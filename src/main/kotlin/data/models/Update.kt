package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Update(
    val id : String ,
    val title : String,
    val text : String ,
    val createdAt : Long,
    val hasRead : Boolean ,
    val userId : String,
    val type : String,
    val navigationId : String
)
