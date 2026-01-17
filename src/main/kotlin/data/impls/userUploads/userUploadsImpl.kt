package com.example.data.impls.userUploads

import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.domain.repositories.userUploads.userUploads
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class userUploadsImpl(private val db : MongoDatabase) : userUploads {
    val posts = db.getCollection<Post>("posts")
    override suspend fun getUserUploads(userId: String): List<Post>{
        try {
            val posts = posts.find(Filters.eq("userId" , userId)).toList()
            return posts
        }
        catch (e : Exception){
            throw e
        }
    }

}