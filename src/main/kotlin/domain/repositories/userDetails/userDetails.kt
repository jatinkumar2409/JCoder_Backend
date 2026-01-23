package com.example.domain.repositories.userDetails

import com.example.data.models.PostDTO
import com.example.data.models.UserDTO

interface userDetails {
    suspend fun getUser(uid : String ,currentUserId : String) : UserDTO?
    suspend fun updateUser(name : String? , bio : String? , uid : String) : Boolean
    suspend fun getUserPosts(uid: String , page:Int , limit:Int) : List<PostDTO>
}
