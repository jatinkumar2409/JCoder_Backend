package com.example.domain.usecases.save

import com.example.domain.repositories.save.saveOrUnsavePost

class savePostUseCase(private val saveOrUnsavePostVal: saveOrUnsavePost) {
    suspend fun savePost(userId : String , postId : String) : Boolean{
        try {
            return saveOrUnsavePostVal.savePost(postId = postId, userId = userId)
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun unSavePost(userId : String ,postId : String) :  Boolean{
        try {
            return saveOrUnsavePostVal.unSavePost(postId = postId , userId = userId)
        }catch ( e : Exception){
   throw e
        }
    }
}