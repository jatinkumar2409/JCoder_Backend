package com.example.domain.usecases.likeAndComment

import com.example.data.models.Like
import com.example.data.utils.generic.ServerExceptions
import com.example.data.utils.generic.retryOperation
import com.example.domain.repositories.likesAndComments.likesAndComments
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class likePostUseCase(private val likesAndCommentsVar: likesAndComments){
    suspend fun likePost(like: Like) : Boolean{
     try {
       return likesAndCommentsVar.likePost(like)
     }catch (e : Exception){
  throw ServerExceptions.LikedStatusFailedException(message = e.message.toString())
     }
    }

    suspend fun unLikePost(like: Like) : Boolean{
        try {
            return likesAndCommentsVar.unLikePost(like)
        }catch ( e : Exception){
            throw ServerExceptions.LikedStatusFailedException(message = e.message.toString())
        }
    }
}