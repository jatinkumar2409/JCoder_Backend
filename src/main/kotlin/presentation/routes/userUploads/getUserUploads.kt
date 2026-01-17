package com.example.presentation.routes.userUploads

import com.example.domain.usecases.userUploads.userUploadsUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.getUserUploads(){
    get("/getUserUploads"){
        println("User uploads is running")
        val token = call.queryParameters["token"] ?: ""
        if (token.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty token")
            return@get
        }
        try {
        val verifyToken by inject<verifyTokenUseCase>()
        val user = verifyToken.verifyToken(token)
        val userUploads by inject<userUploadsUseCase>()
        val posts = userUploads.getUserUploads(user.uid)
            println("Response chala gaya hai")
        call.respond(posts)
    }catch (e : Exception){
        print(e.message.toString())
        call.respond(status = HttpStatusCode.BadRequest , message = "Something went wrong")
    }
    }
}