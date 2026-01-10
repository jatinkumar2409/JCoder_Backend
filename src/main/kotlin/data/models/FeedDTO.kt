package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FeedDTO(
    val posts : List<PostDTO> ,
    val cursor : FeedCursor?
)
