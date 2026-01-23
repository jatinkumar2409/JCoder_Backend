package com.example.domain.repositories.fcmToken

import com.example.data.models.FcmToken
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import com.google.firebase.messaging.Notification

interface fcmServiceRepo {
    suspend fun sendCommentNotification(commentByName : String , token : String , postId : String)
    suspend fun sendFollowNotification(followerName : String , token : String , followingId : String)
    suspend fun sendPostUploadedNotification(authorName : String , token : String , postId : String)
    suspend fun postApprovedOrRejectedNotification(isApproved : Boolean , token: String ,postId : String?)
}