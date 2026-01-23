package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FcmDTO(
    val deviceId : String,
    val appVersion : String ,
    val token : String ,
    val userId : String,
)
