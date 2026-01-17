package com.example.presentation.routes.user

import com.example.domain.usecases.userDetails.getUserPostsUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.getUserPosts(){
    get("/getPosts"){
        try {
        val userId = call.queryParameters["uid"] ?: ""
        val page = call.queryParameters["page"]?.toInt() ?: run {
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty page")
            return@get
        }
            if(userId.trim().isEmpty()){
                call.respond(status = HttpStatusCode.BadRequest , message = "Invalid or missing uid")
                 return@get
            }

         val getPosts by inject<getUserPostsUseCase>()
        val list = getPosts.getUserPosts(userId , page , 16)
            println("\n\n\n list is ${list.joinToString(" , ")} \n\n\n")
        call.respond(list)
        return@get
    }catch (e : Exception){
            e.printStackTrace()
        print("Exception at getPosts is ${e.message}")
        call.respond(status = HttpStatusCode.ExpectationFailed , e.message.toString())
    }
    }

}