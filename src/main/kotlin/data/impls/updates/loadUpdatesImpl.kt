package com.example.data.impls.updates

import com.example.data.models.Update
import com.example.domain.repositories.updates.loadUpdates
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class loadUpdatesImpl(private val db : MongoDatabase) : loadUpdates {
    private val updates = db.getCollection<Update>("updates")
    override suspend fun loadUpdates(userId: String): List<Update> {
        try {
            val response = updates.find(Filters.eq("userId" , userId)).toList()
           return response
        }catch (e : Exception){
            throw e
        }
    }
}