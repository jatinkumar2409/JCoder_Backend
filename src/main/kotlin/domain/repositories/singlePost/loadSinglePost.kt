package com.example.domain.repositories.singlePost

import com.example.data.models.PostDTO

interface loadSinglePost {
    suspend fun loadPostFromDB(postId : String , userId : String) : PostDTO
}