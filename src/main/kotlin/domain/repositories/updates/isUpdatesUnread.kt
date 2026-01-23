package com.example.domain.repositories.updates

import com.example.data.models.Update
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class isUpdatesUnread(private val db : MongoDatabase) {
    private val updates = db.getCollection<Update>("updates")
    suspend fun isUpdatesUnread(userId : String) : Boolean{
        try {
            val response = updates.find(
                Filters.and(
                    Filters.eq("userId" , userId) ,
                    Filters.eq("hasRead" , false)
                )
            ).firstOrNull()
            return response != null
        }catch (e : Exception){
            throw e
        }
    }

    suspend fun setRead(userId: String){
        try {
            updates.updateMany(
                Filters.and(
                    Filters.eq("userId" , userId) ,
                    Filters.eq("hasRead" , false)
                ) ,
                Updates.set("hasRead" , true)
            )
        }catch (e : Exception){
            throw e
        }
    }
}