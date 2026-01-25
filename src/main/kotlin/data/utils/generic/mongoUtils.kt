package com.example.data.utils.generic

import com.example.data.models.Comment
import com.example.data.models.Like
import com.example.data.models.Save
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase


class mongoUtils(private val db : MongoDatabase) {
    private val saved = db.getCollection<Save>("saved")
    private val likes = db.getCollection<Like>("likes")
    private val comments = db.getCollection<Comment>("comments")
    suspend fun deleteSaved(postId : String , userId : String){
        try {
            saved.deleteMany(
                Filters.and(
                    Filters.eq("userId" , userId) ,
                    Filters.eq("postId" , postId)
                )
            )
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun deleteLike(postId : String , userId: String){
        try {
            likes.deleteMany(
                Filters.and(
                    Filters.eq("userId" , userId) ,
                    Filters.eq("postId" , postId)
                )
            )
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun deleteComment(postId: String , userId: String){
        try {
            comments.deleteMany(
                Filters.and(
                    Filters.eq("userId" , userId) ,
                    Filters.eq("postId" , postId)
                )
            )
        }catch (e : Exception){
            throw e
        }
    }
}