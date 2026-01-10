package com.example.domain.usecases.verifyToken

import com.example.data.models.User
import com.example.domain.repositories.addUser.verifyUser

class verifyTokenUseCase(private val verifyUser: verifyUser) {

    fun verifyToken(token : String) : User {
        return verifyUser.addUser(token);
    }
}