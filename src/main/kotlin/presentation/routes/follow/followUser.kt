package com.example.presentation.routes.follow

import com.example.domain.usecases.follow.followUserUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.followUser(){
    get("/changeFollowStatus"){
        println("changeFollow is running")
        val token = call.queryParameters["token"] ?: ""
        val followeeId = call.queryParameters["followeeId"] ?: ""
        val status = call.queryParameters["status"].toBoolean()
        println("followee id is $followeeId and status is $status")

        if(token.trim().isEmpty() || followeeId.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid details")
            return@get
        }
        val followUserUseCase by inject<followUserUseCase>()
        try {
            val verifyToken by inject<verifyTokenUseCase>()
            val userId = verifyToken.verifyToken(token).uid
            if (status){
                println("status is $status")
                followUserUseCase.followUser(followerId = userId , followeeId = followeeId)
                call.respond(status = HttpStatusCode.OK , message = "Follower Added")
            }
            else{
                followUserUseCase.unFollowUser(followerId = userId , followeeId = followeeId)
                call.respond(status = HttpStatusCode.OK , message = "Follower Removed")
            }
        }catch (e : Exception){
            println("Exception at follow user is \n\n\n\n" + e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
        }
    }
}