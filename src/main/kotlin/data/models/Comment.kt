package com.example.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Comment(
    val commentId : String = "" ,
    val userId : String = "",
    val userName : String = "" ,
    val userProfile : String = "",
    val postId : String = "",
    val text : String = "",
    val commentedAt : Long = 0L
)
