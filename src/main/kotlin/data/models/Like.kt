package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Like(
    val postId : String,
    val userId : String
)
