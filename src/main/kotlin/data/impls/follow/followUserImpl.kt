package com.example.data.impls.follow

import com.example.data.impls.fcmToken.fcmServiceImpl
import com.example.data.impls.save.UserInfo
import com.example.data.models.Follow
import com.example.data.models.Update
import com.example.data.models.User
import com.example.data.utils.generic.updatesUtils
import com.example.data.utils.generic.userInfoService
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.example.domain.repositories.follow.followUserRepo
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.inc
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import java.util.UUID

class followUserImpl(private val db : MongoDatabase , private val fcmService: fcmServiceRepo , private val fcmToken : fcmTokenRepo , val userInfo: userInfoService ,
    private val updateUtils_ : updatesUtils) : followUserRepo{
     val follows = db.getCollection<Follow>("follows")
     val users = db.getCollection<User>("users")
    override suspend fun followUser(followeeId: String, followerId: String): Boolean {
        try {
           val a =  follows.insertOne(Follow(followingId = followeeId , followerId = followerId))
            println("\n\n\\n\n\nInseted id is ${a.insertedId}")
            users.updateOne(eq("uid" , followeeId) , inc("follower" , 1))
            users.updateOne(eq("uid" , followerId) , inc("following" ,1))
            val tokens = fcmToken.getTokensByUser(followeeId)
            val userName = userInfo.getUserNameById(followerId)
            tokens.forEach {
                fcmService.sendFollowNotification(followerName = userName , token = it , followingId = followeeId)
            }
            val update =  Update(
                id = UUID.randomUUID().toString() ,
                title = "New Follower!",
                text = "$userName started following you",
                createdAt = System.currentTimeMillis() ,
                hasRead = false ,
                userId = followeeId,
                type = "Follow" ,
                navigationId = followerId
            )
            updateUtils_.addUpdate(update)
            return true
        }catch (e : Exception){
            println("\n\n\n\n\nException at follow user is ${e.message}")
            throw e
        }
    }

    override suspend fun unFollowUser(followeeId: String, followerId: String): Boolean {
        try {
            follows.deleteOne(
                Filters.and(
                    Filters.eq("followingId" , followeeId) ,
                    Filters.eq("followerId" , followerId)
                )
            )
            users.updateOne(eq("uid" , followeeId) , inc("follower" , -1))
            users.updateOne(eq("uid" , followerId) , inc("following" ,-1))
            return true
        }catch (e : Exception){
            throw e
        }
    }
}