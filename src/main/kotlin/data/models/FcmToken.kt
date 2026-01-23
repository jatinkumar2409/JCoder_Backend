package com.example.data.models

import kotlinx.serialization.Serializable


@Serializable
data class FcmToken(
    val deviceId : String = "",
    val token : String = "" ,
    val appVersion : String = "" ,
    val userId : String = "",
    val lastUsed : Long = 0L ,
    val createdAt : Long = 0L
)
