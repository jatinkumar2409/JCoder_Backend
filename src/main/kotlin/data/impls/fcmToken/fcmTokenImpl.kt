package com.example.data.impls.fcmToken

import com.example.data.models.FcmDTO
import com.example.data.models.FcmToken
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.Instant

class fcmTokenImpl(private val db : MongoDatabase) : fcmTokenRepo {
   private val fcmToken = db.getCollection<FcmToken>("fcm_tokens")
    override suspend fun upsertToken(
        tokenDto : FcmDTO
    ): Boolean {
        try {

            val filters = eq("deviceId" , tokenDto.deviceId)
            val updates = Updates.combine(
                Updates.set("userId" , tokenDto.userId) ,
                Updates.set("token" , tokenDto.token) ,
                Updates.set("appVersion" ,tokenDto.appVersion),
                Updates.set("lastUsed" , System.currentTimeMillis()) ,
                Updates.setOnInsert("createdAt" , System.currentTimeMillis())
            )
            fcmToken.updateOne(
                filters , updates , UpdateOptions().upsert(true)
            )
            return true
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun getTokensByUser(userId: String): List<String> {
   try {
     return fcmToken.find(eq("userId" , userId)).map { it.token }.toList()
   }
   catch (e : Exception){
       throw e
   }
    }

    override suspend fun deleteTokens(deviceId: String) {
      try {
        fcmToken.deleteOne(eq("deviceId" , deviceId))
      }
      catch(e : Exception){
          throw e
      }
    }

    override suspend fun deleteStaleTokens(days: Long) {
        val cutoff = Instant.now().minusSeconds(days * 24 * 60 * 60).toEpochMilli()
        fcmToken.deleteMany(Filters.lt("lastSeen" , cutoff))
    }
}