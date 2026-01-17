package com.example.domain.usecases.userUploads

import com.example.data.models.Post
import com.example.domain.repositories.userUploads.userUploads

class userUploadsUseCase(private val userUploadsVal: userUploads){
    suspend fun getUserUploads(userId : String) : List<Post>{
        return try {
            userUploadsVal.getUserUploads(userId)
        }catch (e : Exception){
            throw e
        }
    }
}