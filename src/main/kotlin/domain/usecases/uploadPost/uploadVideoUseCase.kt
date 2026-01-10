package com.example.domain.usecases.uploadPost

import com.example.data.models.Post
import com.example.data.models.User
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.example.domain.repositories.postUpload.uploadPosts
import java.io.File

class uploadVideoUseCase(private val verifyVideo : sightEngine , private val uploadVideo : uploadPosts , private val uploadPostToMongo : uploadPostToMongo) {
    suspend fun uploadVideo(file : File ,caption : String , user : User , tags : String , tagsList : List<String>){
        val postId = verifyVideo.verifyVideo(file , caption , "https://jolliest-deedra-hawkish.ngrok-free.dev/sightengine/callback" , user.uid)
        val post = Post(
            postId = postId,
            userId = user.uid,
            caption = caption + tags,
            imageUrls = emptyList(),
            videoUrl = "",
            contentType = "video",
            likes = 0,
            tags = tagsList,
            status = "private",
            createdAt = System.currentTimeMillis() ,
            expiredAt = System.currentTimeMillis() + (2 * 60 * 60 * 1000)
        )
        uploadPostToMongo.uplaodPost(post)
        print(post)
    }
}
