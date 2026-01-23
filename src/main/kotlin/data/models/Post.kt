package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val postId : String = "" ,
    val userId : String = "",
    val caption : String = "",
    val contentType : String = "text",
    val imageUrls : List<String> = emptyList(),
    val videoUrl : String = "" ,
    val likes : Int = 0,
    val comments : Int = 0,
    val tags : List<String> = emptyList(),
    val status : String = "public",
    val createdAt : Long = 0L,
    val expiredAt : Long? = null
)
