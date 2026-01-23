package com.example.data.utils.generic

import com.example.data.models.Follow
import com.example.data.models.Post
import com.example.data.models.User
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.koin.core.component.getScopeName

class userInfoService(private val db : MongoDatabase){
    val posts = db.getCollection<Post>("posts")
    val users = db.getCollection<User>("users")
    val follows = db.getCollection<Follow>("follows")
    suspend fun getOwnerByPostId(postId : String) : String{
         try {
             val response = posts.find(eq(
                 "postId" , postId
             )).first()
             return response.userId
         }catch (e : Exception){
             throw e
         }
    }
    suspend fun getUserNameById(userId : String) : String{
        try {
            val response = users.find(eq(
                "uid" , userId
            )).first()
            return response.name
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun getFollowersIds(userId : String) : List<String>{
        try {
            val response = follows.find(eq("followingId" , userId)).toList().map { it.followerId }
            return response
        }catch (e : Exception){
            throw e
        }
    }
}