package com.example.domain.usecases.uploadPost

import com.example.data.models.Post
import com.example.data.models.User
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.example.domain.repositories.postUpload.uploadPosts
import java.io.File
import java.util.UUID

class uploadImageUseCase(private val verifyImage : sightEngine , private val uploadImages : uploadPosts , private val uploadPostToMongo : uploadPostToMongo) {
  suspend fun uploadImage(files : List<File> , caption : String , user : User , tags : String , tagsList : List<String>){
        val fileBytes = files.map { it.readBytes() }
        val response = verifyImage.verifyPost(files, caption)
        println(response)
        val imagesLinks = uploadImages.uploadImages(fileBytes, user.uid)
        val post = Post(
            postId = UUID.randomUUID().toString(),
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
        print(post)
    }
}