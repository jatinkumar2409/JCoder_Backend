package com.example.domain.repositories.follow

interface followUserRepo {
    suspend fun followUser(followeeId : String , followerId : String) : Boolean
    suspend fun unFollowUser(followeeId: String , followerId: String) : Boolean
}