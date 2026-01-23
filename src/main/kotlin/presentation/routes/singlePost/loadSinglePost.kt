package com.example.presentation.routes.singlePost

import com.example.domain.usecases.singlePost.loadPostUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.loadSinglePost(){
    get("/getPost"){
        val postId = call.queryParameters["postId"] ?: ""
        val userId = call.queryParameters["userId"] ?: ""
        if (postId.trim().isEmpty() || userId.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty post id")
         return@get
        }
        try {
            val loadPostUseCase by inject<loadPostUseCase>()
            val post = loadPostUseCase.loadPost(postId, userId)
            call.respond(post)
        }catch (e : Exception){
            println("Exception at sending post is ${e.message}")
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
        }

    }
}