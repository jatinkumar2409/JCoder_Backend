package com.example.data.utils.user

import com.example.data.models.User
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class updateUser(private val db : MongoDatabase) {
    val collection = db.getCollection<User>("users")
    suspend fun updateOneField(field : String , data : Any, userId : String) : Boolean{
        val result = collection.updateOne(eq("uid" , userId) , Updates.set(field , data))
        println("\n\n The updated result is ${result.upsertedId} and ${result.modifiedCount}")
        return result.wasAcknowledged()
    }
}