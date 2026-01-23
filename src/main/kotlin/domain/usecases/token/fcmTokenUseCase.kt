package com.example.domain.usecases.token

import com.example.data.models.FcmDTO
import com.example.domain.repositories.fcmToken.fcmTokenRepo

class addTokenUseCase(private val fcmToken : fcmTokenRepo){
    suspend fun addFcmToken(token : FcmDTO) : Boolean{
        try {
           return fcmToken.upsertToken(token)
        }catch (e : Exception){
            throw e
        }
    }
}