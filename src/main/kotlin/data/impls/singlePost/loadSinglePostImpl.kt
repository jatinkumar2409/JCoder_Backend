package com.example.data.impls.singlePost

import com.example.data.helpers.toPostDto
import com.example.data.models.Like
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.models.Save
import com.example.data.models.User
import com.example.domain.repositories.singlePost.loadSinglePost
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class loadSinglePostImpl(private val db : MongoDatabase) : loadSinglePost {
    private val posts = db.getCollection<Post>("posts")
    private val likes = db.getCollection<Like>("likes")
    private val saved = db.getCollection<Save>("saved")
    private val users = db.getCollection<User>("users")
    override suspend fun loadPostFromDB(postId: String ,userId : String): PostDTO {
        try {
            val response = posts.find(Filters.eq("postId" , postId)).firstOrNull() ?: throw IllegalArgumentException("Post not found")
            val postDto = response.toPostDto()
            val isLiked = likes.find(Filters.and(
                Filters.eq("postId" , postId),
                Filters.eq("userId" ,userId)
            )).firstOrNull()
            val isSaved = saved.find(Filters.and(
                Filters.eq("postId" , postId),
                Filters.eq("userId" ,userId)
            )).firstOrNull()
            val user = users.find(Filters.eq("uid" , userId)).firstOrNull() ?: throw IllegalArgumentException("User not found")
            return postDto.copy(isLiked = isLiked != null , isSaved = isSaved != null, userName = user.name , userProfile = user.profilePicture)
        }catch (e : Exception){
            throw e
        }
    }

}