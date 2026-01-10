package com.example.domain.repositories.userDetails

import com.example.data.models.User

interface userDetails {
    suspend fun getUser(uid : String) : User?
    suspend fun updateUser(user : User) : Boolean
}
