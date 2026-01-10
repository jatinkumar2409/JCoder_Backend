package com.example.domain.repositories.addUser

import com.example.data.models.User

interface verifyUser {
    fun addUser(token : String) : User
}