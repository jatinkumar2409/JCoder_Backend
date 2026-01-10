package com.example.domain.repositories.save

import com.example.data.models.Post
import com.example.data.models.PostDTO

interface saveOrUnsavePost {
    suspend fun savePost(postId : String , userId : String) : Boolean
    suspend fun unSavePost(postId : String , userId : String) : Boolean
}