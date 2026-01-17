package com.example.domain.usecases.userDetails

import com.example.domain.repositories.userDetails.userDetails

class updateUserUseCase(private val userDetailsVal : userDetails) {
    suspend fun updateUser(uid : String , name : String? , bio : String?) : Boolean{
        return try {
            userDetailsVal.updateUser(name = name , bio = bio , uid = uid)
        }catch (e : Exception){
            throw e
        }
    }
}