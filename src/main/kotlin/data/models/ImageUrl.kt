package com.example.data.models

import kotlinx.serialization.Serializable


@Serializable
data class ImageUrl(
    val thumbnail : String,
    val main : String ,
    val chat : String
)
