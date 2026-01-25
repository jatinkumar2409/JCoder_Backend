package com.example.presentation.routes.user

import com.example.domain.usecases.userDetails.deletePostUseCase
import com.example.domain.usecases.userDetails.getUserPostsUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.apache.hc.core5.http.HttpStatus
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
     get("/deletePost"){
       val postId = call.queryParameters["postId"] ?: run{
           call.respond(status = HttpStatusCode.BadRequest , message = "Invalid post id")
           return@get
       }
         val token = call.queryParameters["token"] ?: run{
             call.respond(status = HttpStatusCode.BadRequest , message = "Invalid post id")
             return@get
         }
         val deletePost by inject<deletePostUseCase>()
         val verifyUser by inject<verifyTokenUseCase>()
         try {
             val user = verifyUser.verifyToken(token)
             deletePost.deletePost(postId = postId , userId = user.uid)
             call.respond(status = HttpStatusCode.OK , message = "Post deleted")
         }catch (e : Exception){
             call.respond(status = HttpStatusCode.OK , message = "Something went wrong")
         }
     }
}