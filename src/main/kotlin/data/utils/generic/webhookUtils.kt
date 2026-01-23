package com.example.data.utils.generic

import com.example.data.models.Update
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import java.util.UUID

class webhookUtils(private val userInfo : userInfoService , private val fcmService : fcmServiceRepo ,
    private val fcmToken : fcmTokenRepo ,private val updatesUtils_: updatesUtils) {
    suspend fun notifyFollowers(postId : String ,userId : String){
        try {
            val followerIds = userInfo.getFollowersIds(userId)
            val authorName = userInfo.getUserNameById(userId)
            val tokens = mutableListOf<String>()
            followerIds.forEach {
                tokens.addAll(fcmToken.getTokensByUser(it))
            }
            tokens.toList().forEach {
                fcmService.sendPostUploadedNotification(authorName = authorName , token = it , postId = postId)
            }
            val updates = followerIds.map{
                Update(
                    id = UUID.randomUUID().toString() ,
                    title = "New Post!",
                    text = "$authorName just uploaded a new post",
                    createdAt = System.currentTimeMillis() ,
                    hasRead = false ,
                    userId = it ,
                    type = "New Post" ,
                    navigationId = postId
                )
            }
            updatesUtils_.addUpdates(updates)
        }catch (e : Exception){
            throw e
        }
    }

    suspend fun notifyAuthor(status : Boolean , postId : String? , userId : String){
        try {
            val tokens = fcmToken.getTokensByUser(userId)
            tokens.forEach {
                fcmService.postApprovedOrRejectedNotification(isApproved = status , postId = postId , token = it)
            }
            val update =  Update(
                id = UUID.randomUUID().toString() ,
                title = if (status) "Congratulations!" else "Hard Luck!",
                text = if (status) "Your video post has been approved" else "Your post has been rejected because the moderation api found your content unsuitable",
                createdAt = System.currentTimeMillis() ,
                hasRead = false ,
                userId = userId ,
                type = "New Post" ,
                navigationId = postId ?: ""
            )
            updatesUtils_.addUpdate(update)
        }
        catch (e : Exception){
            throw e
        }
    }
}