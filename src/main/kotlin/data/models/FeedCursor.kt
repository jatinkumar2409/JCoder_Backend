package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FeedCursor(
    val score : Double ,
    val createdAt : Long ,
    val postId : String,
)
