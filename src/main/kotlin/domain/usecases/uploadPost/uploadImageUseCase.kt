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
import com.example.domain.repositories.postUpload.uploadPosts
import java.io.File
import java.util.UUID

class uploadImageUseCase(private val verifyImage : sightEngine , private val uploadImages : uploadPosts , private val uploadPostToMongo : uploadPostToMongo ,
    private val userInfo: userInfoService ,private val fcmService: fcmServiceRepo , private val fcmToken : fcmTokenRepo , private val updatesUtilsVal: updatesUtils) {
  suspend fun uploadImage(files : List<File> , caption : String , user : User , tags : String , tagsList : List<String>){
      try {
          val fileBytes = files.map { it.readBytes() }
          val response = verifyImage.verifyPost(files, caption)
          println(response)
          val imagesLinks = uploadImages.uploadImages(fileBytes, user.uid)
          val postId = UUID.randomUUID().toString()
          val post = Post(
              postId = postId,
              userId = user.uid,
              caption = caption + tags,
              imageUrls = imagesLinks,
              videoUrl = "",
              contentType = "image",
              likes = 0,
              tags = tagsList,
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
          followerIds.forEach {
              updatesUtilsVal.addUpdate(
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
              )
          }
      }catch (e : Exception) {
          throw e
      }
    }
}