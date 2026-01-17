package com.example.presentation.routes.save

import com.example.domain.usecases.save.loadSavedPosts
import com.example.domain.usecases.save.savePostUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
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
    post("/loadSavedPosts"){
      val savePostDto = call.receive<SavePostDTO>()
        try {
            val verifyToken by inject<verifyTokenUseCase>()
            val loadPosts by inject<loadSavedPosts>()
            val user = verifyToken.verifyToken(savePostDto.token)
            val posts = loadPosts.loadSavedPosts(userId = user.uid , page = savePostDto.page, limit = 16)
            call.respond(posts)
        }catch (e : Exception){
            println("\n\n\n\n Exception at load save posts is" + e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
            return@post
        }
    }
}

@Serializable
data class SavePostDTO(
    val page : Int ,
    val token : String
)