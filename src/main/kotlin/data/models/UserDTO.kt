package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    var uid : String = "" ,
    var name : String = "" ,
    var email : String = "" ,
    var profilePicture : String = "",
    var bio : String = "" ,
    var postCount : Int = 0 ,
    var following : Int = 0 ,
    var follower : Int = 0,
    val isFollowed : Boolean = false
)

