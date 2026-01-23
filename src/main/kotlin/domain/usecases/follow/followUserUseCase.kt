package com.example.domain.usecases.follow

import com.example.domain.repositories.follow.followUserRepo

class followUserUseCase(private val followUserRepoVal: followUserRepo) {
    suspend fun followUser(followerId : String , followeeId : String) : Boolean{
        try {
           return followUserRepoVal.followUser(followerId = followerId , followeeId = followeeId)
        }
        catch (e : Exception){
            throw e
        }
    }
    suspend fun unFollowUser(followerId: String , followeeId: String) : Boolean{
        try {
           return followUserRepoVal.unFollowUser(followerId = followerId , followeeId = followeeId)
        }catch (e : Exception){
            throw e
        }
    }
}