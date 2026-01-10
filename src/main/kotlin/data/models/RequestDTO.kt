package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestDTO(
    val feedCursor: FeedCursor? , val token : String
)
