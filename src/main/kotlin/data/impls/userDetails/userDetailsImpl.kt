package com.example.data.impls.userDetails

import com.example.data.helpers.toPostDto
import com.example.data.impls.save.LikesPostIds
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.models.User
import com.example.domain.repositories.userDetails.userDetails
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.`in`
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

class userDetailsImpl(private val db : MongoDatabase) : userDetails{
   private val users = db.getCollection<User>("users")
    private val posts = db.getCollection<Post>("posts")
    val likes = db.getCollection<LikesPostIds>("likes")
    val saved = db.getCollection<SavedPostsIds>("saved")
    override suspend fun getUser(uid: String): User? {
        try {
            val user = users.find(eq("uid" , uid)).firstOrNull()
            return user
        }catch (e : Exception){
            print("Excpetion at getUser is : ${e.message}")
            throw e
        }
    }

    override suspend fun updateUser(name : String? , bio : String? , uid : String): Boolean {
        val updates = mutableListOf<Bson>()
      name?.let {
          updates.add(Updates.set("name", name))
      }
        bio?.let {
            updates.add(Updates.set("bio", bio))
        }
        try {
            users.updateOne(eq("uid" , uid) , Updates.combine(updates))
            return true
        }catch (e : Exception){
            throw e
        }

    }

    override suspend fun getUserPosts(uid: String , page:Int , limit:Int): List<PostDTO>{
        println("get User posts called with page $page")
        try {
            val response = posts.find(
                Filters.and(
                    Filters.eq("userId" , uid) ,
                    Filters.eq("status" , "public")
                )
            ).skip(limit * page).limit(limit)
                .toList().map { it.toPostDto() }
           val likedPostIds = likes.find(
               Filters.and(
                   eq("userId" , uid),
                   `in`("postId" , response.map { it.postId })
               )
           ).projection(include("postId")).toList().map {
               it.postId
           }.toSet()
            val savedPostIds = saved.find(
                Filters.and(
                    eq("userId" , uid) ,
                    `in`("postId" , response.map { it.postId })
                )
            ).projection(include("postId")).toList().map {
                it.postId
            }.toSet()
             return response.map {
                 it.copy(isLiked = likedPostIds.contains(it.postId) , isSaved = savedPostIds.contains(it.postId))
             }
        }
        catch (e : Exception){
            throw e
        }
    }

}

@Serializable
data class SavedPostsIds(val postId : String)