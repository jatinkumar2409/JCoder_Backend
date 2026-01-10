package com.example.presentation.routes.save

import com.example.domain.usecases.save.savePostUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.saveOrUnsavePost(){
    get("/saveOrUnsavePost"){
      val userId = call.queryParameters["userId"] ?: ""
      val postId = call.queryParameters["postId"] ?: ""
      val status = call.queryParameters["status"].toBoolean()

      if (userId.trim().isEmpty() || postId.trim().isEmpty()){
          call.respond(status = HttpStatusCode.BadRequest , message = "Invalid details")
          return@get
      }
        try {
            val savePostUseCase by inject<savePostUseCase>()
            if (status) {
                savePostUseCase.savePost(userId, postId)
                call.respond(status = HttpStatusCode.OK, message = "Post Saved")
                return@get
            } else {
                savePostUseCase.unSavePost(userId, postId)
                call.respond(status = HttpStatusCode.OK, message = "Post Unsaved")
                return@get
            }
        }catch (e : Exception){
            println(e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
            return@get
        }
    }
}