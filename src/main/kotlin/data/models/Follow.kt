package com.example.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Follow(
    val followerId : String ,
    val followingId : String
)
