package com.example.domain.usecases.userDetails

import com.example.data.models.Post
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class getUserPostsUseCase(private val db : MongoDatabase) {
    val posts = db.getCollection<Post>("posts")
    suspend fun getUserPosts(uid : String) : List<Post>{
        try {
            val list = posts.find(eq("userId" , uid)).toList()
            return list
        }catch (e : Exception){
            print("The expception is ${e.message}")
              throw e
            return emptyList()
        }
    }

}