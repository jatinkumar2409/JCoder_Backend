package com.example.data.impls.save

import com.example.data.models.Save
import com.example.domain.repositories.save.saveOrUnsavePost
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class savePostImpl(private val db : MongoDatabase) : saveOrUnsavePost {
    val saved = db.getCollection<Save>("saved")
    override suspend fun savePost(postId: String, userId: String): Boolean {
        try {
            saved.insertOne(Save(postId , userId))
            return true
        }catch (e : Exception){
            throw e
        }
    }

    override suspend fun unSavePost(postId: String, userId: String): Boolean {
        try {
            saved.deleteOne(Filters.and(
                Filters.eq("postId" , postId) , Filters.eq("userId" , userId)
            ))
            return true
        }catch (e : Exception){
            throw e
        }
    }
}