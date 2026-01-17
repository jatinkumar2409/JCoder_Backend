package com.example.domain.usecases.userDetails

import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.domain.repositories.userDetails.userDetails
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class getUserPostsUseCase(private val userDetailsVal: userDetails) {

    suspend fun getUserPosts(uid : String , page : Int , limit : Int) : List<PostDTO>{
        try {
            return userDetailsVal.getUserPosts(uid = uid , page = page , limit = limit)
        }catch (e : Exception){
            print("The expception is ${e.message}")
              throw e
        }
    }

}