package com.example.data.impls.fcmToken

import com.example.data.models.Update
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import com.google.firebase.messaging.Notification
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import java.time.Clock.system
import java.util.UUID

class fcmServiceImpl(
    private val fcmToken: fcmTokenRepo
) : fcmServiceRepo {
    override suspend fun sendCommentNotification(
        commentByName: String,
        token: String,
        postId: String
    ) {
        println("send comment is working")
        val message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle("New Comment!")
                    .setBody("$commentByName commented on your post")
                    .build()
            )
            .putData("type", "Comment")
            .putData("id", postId)
            .build()
        try {
            FirebaseMessaging.getInstance().send(message)
            println("i am running with token $token")
        }catch (e : FirebaseMessagingException){
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED){
                fcmToken.deleteTokens(token)
            }
        }
        catch (e : Exception){
            println("\n\n\n\n\nException at send comment is ${e.message}")

        }
    }

    override suspend fun sendFollowNotification(
        followerName: String,
        token: String,
        followingId: String
    ) {
        val message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle("New Follower!")
                    .setBody("$followerName started following you")
                    .build()
            )
            .putData("type" , "Follow")
            .putData("id" , followingId)
            .build()

        try {
            FirebaseMessaging.getInstance().send(message)
        }catch (e : FirebaseMessagingException){
         if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED){
             fcmToken.deleteTokens(token)
         }
        }
        catch (e : Exception){

        }
    }

    override suspend fun sendPostUploadedNotification(
        authorName: String,
        token: String,
        postId: String
    ) {
        val message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle("New Post!")
                    .setBody("$authorName just uploaded a post")
                    .build()
            )
            .putData("type" , "Post Uploaded")
            .putData("id" , postId)
            .build()

        try {
            FirebaseMessaging.getInstance().send(message)
        }
        catch (e : FirebaseMessagingException){
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED){
                fcmToken.deleteTokens(token)
            }
        }
        catch (e : Exception){

        }
    }

    override suspend fun postApprovedOrRejectedNotification(
        isApproved: Boolean,
        token: String ,
        postId : String?
    ) {
        val message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle(if (isApproved) "Congratulations!" else "Hard Luck!")
                    .setBody(if (isApproved) "Your video post has been approved" else "Your post has been rejected by the moderation api")
                    .build()
            )
            .putData("type" , "Post Approval")
            .putData("id" , postId)
            .build()

         try {
             FirebaseMessaging.getInstance().send(message)
         }catch (e : FirebaseMessagingException){
             if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED){
                 fcmToken.deleteTokens(token)
             }
         }
         catch (e : Exception){

         }
    }
}