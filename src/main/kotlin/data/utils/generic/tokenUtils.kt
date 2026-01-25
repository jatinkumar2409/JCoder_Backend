package com.example.data.utils.generic

import com.example.data.models.FcmToken
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class tokenUtils(private val db : MongoDatabase) {
   private val fcmTokens = db.getCollection<FcmToken>("fcm_tokens")
    suspend fun updateLastUsed(token : String){
        try {
            fcmTokens.updateOne(
                Filters.eq("token" , token) , Updates.set("lastUsed" , System.currentTimeMillis())
            )
        }catch (e : Exception){}
    }

}