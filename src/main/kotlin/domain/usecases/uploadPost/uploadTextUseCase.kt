package com.example.domain.usecases.uploadPost

import com.example.data.models.Post
import com.example.data.models.Update
import com.example.data.models.User
import com.example.data.utils.generic.updatesUtils
import com.example.data.utils.generic.userInfoService
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.google.firebase.database.core.UserWriteRecord
import java.util.UUID

class uploadTextUseCase(private val verifyText : sightEngine , private val uploadPostToMongo: uploadPostToMongo ,
    private val fcmToken : fcmTokenRepo , private val fcmService : fcmServiceRepo ,private val userInfo : userInfoService ,
    private val updatesUtilsVal : updatesUtils) {
    suspend fun uploadText(caption : String , tags : String , user : User , tagsList : List<String>){
        try {
            val verifyText = verifyText.verifyText(text = caption + tags)
            print(verifyText)
            val postId =  UUID.randomUUID().toString()
            val post = Post(
                postId = postId,
                userId = user.uid,
                caption = caption,
                tags = tagsList,
                status = "public",
                contentType = "text",
                createdAt = System.currentTimeMillis()
            )
            uploadPostToMongo.uplaodPost(post)
            val followerIds = userInfo.getFollowersIds(user.uid)
            val tokens = mutableListOf<String>()
            followerIds.forEach {
                tokens.addAll(fcmToken.getTokensByUser(it))
            }
            tokens.toList().forEach {
                fcmService.sendPostUploadedNotification(authorName = user.name , token = it , postId = postId)
            }
         val updates = followerIds.map {
                    Update(
                        id = UUID.randomUUID().toString(),
                        title = "New Post!",
                        text = "${user.name} just uploaded a post",
                        createdAt = System.currentTimeMillis(),
                        hasRead = false,
                        type = "Post Uploaded",
                        userId = it,
                        navigationId = postId,
                    )
            }
            updatesUtilsVal.addUpdates(updates)
            print(post)
        }catch (e : Exception){
            throw e
        }
    }
}