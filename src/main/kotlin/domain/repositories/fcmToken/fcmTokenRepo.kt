package com.example.domain.repositories.fcmToken

import com.example.data.models.FcmDTO

interface fcmTokenRepo {
    suspend fun upsertToken(tokenDto : FcmDTO) : Boolean
    suspend fun getTokensByUser(userId: String) : List<String>
    suspend fun deleteTokens(deviceId : String)
    suspend fun deleteStaleTokens(days : Long)
}