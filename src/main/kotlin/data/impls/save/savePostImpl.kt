package com.example.data.impls.save

import com.example.data.helpers.toPostDto
import com.example.data.models.Like
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.models.Save
import com.example.data.models.User
import com.example.domain.repositories.save.saveOrUnsavePost
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.`in`
import com.mongodb.client.model.Projections.include
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.serialization.Serializable

class savePostImpl(private val db : MongoDatabase) : saveOrUnsavePost {
    val saved = db.getCollection<Save>("saved")
    val posts = db.getCollection<Post>("posts")
    val likes = db.getCollection<LikesPostIds>("likes")
    val users = db.getCollection<UserInfo>("users")
    override suspend fun savePost(postId: String, userId: String): Boolean {
        try {
            saved.insertOne(Save(postId , userId , System.currentTimeMillis()))
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

    override suspend fun loadSavedPosts(userId: String , page : Int ,limit : Int): List<PostDTO> {
        try {
            val response = saved.find(eq("userId", userId)).toList()
            val posts = posts.find(`in`("postId" , response.map { it.postId })).skip(page * limit).limit(limit).toList().map { it.toPostDto() }
            val likedPostIds = likes
                .find(
                    and(
                        eq("userId", userId),
                        `in`("postId", response.map { it.postId })
                    )
                )
                .projection(include("postId"))
                .map { it.postId }
                .toSet()
            val usersIds = posts.map { it.userId }.toSet()

            val userMap = users.find(`in`("uid" , usersIds))
                .projection(include("uid", "name", "profilePicture"))
                .toList().associateBy { it.uid }
             return posts.map {
                  it.copy(isLiked = likedPostIds.contains(it.postId) , isSaved = true , userName = userMap[it.userId]?.name ?: "" , userProfile = userMap[it.userId]?.profilePicture ?: "")
              }
        }catch (e : Exception){
            throw e
        }
    }
}
@Serializable
 data class LikesPostIds(val postId : String)
@Serializable
 data class UserInfo(var uid : String = "" , var name : String = "" ,  val profilePicture : String)