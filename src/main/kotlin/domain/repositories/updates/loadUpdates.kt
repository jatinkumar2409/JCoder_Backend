package com.example.domain.repositories.updates

import com.example.data.models.Update

interface loadUpdates {
    suspend fun loadUpdates(userId : String) : List<Update>
}