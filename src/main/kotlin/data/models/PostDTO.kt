package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PostDTO(
    val postId : String = "" ,
    val userId : String = "",
    val caption : String = "",
    val contentType : String = "text",
    val imageUrls : List<ImageUrl> = emptyList(),
    val videoUrl : String = "" ,
    val likes : Int = 0,
    val comments : Int = 0,
    val tags : List<String> = emptyList(),
    val status : String = "public",
    val createdAt : Long = System.currentTimeMillis(),
    val expiredAt : Long? = null,
    val score : Double = 0.0,
    val isLiked : Boolean = false ,
    val userName : String ,
    val userProfile : String ,
    val isSaved : Boolean = false
)
