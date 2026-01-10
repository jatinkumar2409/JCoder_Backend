package com.example.domain.repositories.addUser

import com.example.data.models.User

interface addUser {
    suspend fun addOrUpdateUser(user : User) : Boolean
}