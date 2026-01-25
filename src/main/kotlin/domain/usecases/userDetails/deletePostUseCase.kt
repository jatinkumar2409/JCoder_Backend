package com.example.domain.usecases.userDetails

import com.example.data.utils.generic.mongoUtils
import com.example.domain.repositories.postUpload.uploadPostToMongo

class deletePostUseCase(private val uploadPostToMongoVal: uploadPostToMongo , private val mongoUtilsVal : mongoUtils) {
    suspend fun deletePost(postId : String , userId : String){
        try {
            uploadPostToMongoVal.deletePost(postId , userId)
            mongoUtilsVal.deleteComment(postId , userId)
            mongoUtilsVal.deleteLike(postId , userId)
            mongoUtilsVal.deleteSaved(postId , userId)
        }catch (e : Exception){
            throw e
        }
    }
}