package com.example.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Save(
    val postId : String , val userId : String , val savedAt : Long
)
