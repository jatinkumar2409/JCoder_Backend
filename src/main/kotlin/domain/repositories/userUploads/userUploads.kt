package com.example.domain.repositories.userUploads

import com.example.data.models.Post
import com.example.data.models.PostDTO

interface userUploads {
    suspend fun getUserUploads(userId : String) : List<Post>
}