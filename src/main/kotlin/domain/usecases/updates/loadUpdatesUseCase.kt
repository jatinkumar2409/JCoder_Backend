package com.example.domain.usecases.updates

import com.example.data.models.Update
import com.example.domain.repositories.updates.loadUpdates

class loadUpdatesUseCase(private val loadUpdatesVal: loadUpdates) {
    suspend fun loadUpdates(userId : String) : List<Update>{
        try {
            return loadUpdatesVal.loadUpdates(userId)
        }catch (e : Exception){
            throw e
        }
    }
}