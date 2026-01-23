package com.example.domain.usecases.userDetails

import com.example.data.models.User
import com.example.data.models.UserDTO
import com.example.domain.repositories.userDetails.userDetails

class getUserUseCase(private val user: userDetails) {
    suspend fun getUser(uid : String , currentUserId : String) : UserDTO?{
        return user.getUser(uid , currentUserId)
    }
}