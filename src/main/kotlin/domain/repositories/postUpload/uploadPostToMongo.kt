package com.example.domain.repositories.postUpload

import com.example.data.models.ImageUrl
import com.example.data.models.Post

interface uploadPostToMongo{
    suspend fun uplaodPost(post : Post) : Post
    suspend fun updatePost(postId : String , videoUrl : String ,status : String) : Boolean
    suspend fun deletePost(postId: String , userId : String) : Boolean
}