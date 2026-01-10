package com.example.data.impls.addUser

import com.example.data.models.User
import com.example.domain.repositories.addUser.addUser
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.client.model.Filters.eq

import com.mongodb.client.model.ReplaceOptions

class addUserImpl(private  val db : MongoDatabase) : addUser {
    override suspend fun addOrUpdateUser(user: User): Boolean{
        try {
            val col = db.getCollection<User>("users")
            val result = col.replaceOne(
                filter = eq("uid", user.uid),
                replacement = user,
                options = ReplaceOptions().upsert(true)
            )
            return result.wasAcknowledged()
        }catch (e : Exception){
            throw e
        }
    }
}