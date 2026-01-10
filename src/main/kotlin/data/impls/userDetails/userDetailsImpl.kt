package com.example.data.impls.userDetails

import com.example.data.models.User
import com.example.domain.repositories.userDetails.userDetails
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class userDetailsImpl(private val db : MongoDatabase) : userDetails{
    override suspend fun getUser(uid: String): User? {
        try {
            val collection = db.getCollection<User>("users")
            val user = collection.find(eq("uid" , uid)).firstOrNull()
            return user
        }catch (e : Exception){
            print("Excpetion at getUser is : ${e.message}")
            throw e
        }
    }

    override suspend fun updateUser(user: User): Boolean {
  return true
    }

}