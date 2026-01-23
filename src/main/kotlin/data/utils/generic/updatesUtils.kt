package com.example.data.utils.generic

import com.example.data.models.Update
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class updatesUtils(private val db : MongoDatabase) {
    private val updates = db.getCollection<Update>("updates")
    suspend fun addUpdate(update: Update){
        try {
            updates.insertOne(update)
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun addUpdates(updates_: List<Update>){
        try {
            updates.insertMany(updates_)
        }catch (e : Exception){
            throw e
        }
    }

}