package com.example.presentation.routes.likeAndComments

import com.example.data.models.Comment
import com.example.data.models.Like
import com.example.domain.usecases.likeAndComment.commentsUseCase
import com.example.domain.usecases.likeAndComment.likePostUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.changeLikeAndCommentStatus(){
   post("/changePostLikeStatus"){
       println("hitting likePostEndpoint")
       val status = call.queryParameters["status"].toBoolean()
       try {
           val like = call.receive<Like>()
           if (like.postId.trim().isEmpty() || like.userId.trim().isEmpty()) {
               call.respond(status = HttpStatusCode.BadRequest, message = "Invalid details")
               return@post
           }
           val likePost by inject<likePostUseCase>()
           if(status) {
               likePost.likePost(like)
           }
           if(!status){
               likePost.unLikePost(like)
           }
           call.respond(status = HttpStatusCode.OK , message = "Post Liked Status Changed")
       }
       catch (e : Exception){
           call.respond(status = HttpStatusCode.ExpectationFailed , message = e.message.toString())
       }
    }

   get("/getComments"){
    val postId = call.queryParameters["postId"] ?: ""
       if(postId.trim().isEmpty()){
           call.respond(status = HttpStatusCode.BadRequest , message = "Invalid postId")
           return@get
       }
       try {
           val commentsUseCase by inject<commentsUseCase>()
           val comments = commentsUseCase.loadComments(postId)
           call.respond(comments)
       }catch (e : Exception){
           println(e.message.toString())
           call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
       }
   }
    post("/addComment"){
        val token = call.request.headers["token"] ?: ""
        if(token.trim().isEmpty()){
            println("\n\n Token is $token")
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid token")
            return@post
        }
        try {
            val verifyUser by inject<verifyTokenUseCase>()
            val user = verifyUser.verifyToken(token)
            val commentsUseCase by inject<commentsUseCase>()
            var comment = call.receive<Comment>()
            comment = comment.copy(commentedAt = System.currentTimeMillis() , userProfile = user.profilePicture , userName = user.name , userId = user.uid)
            val commentsResponse = commentsUseCase.addComments(comment)
            call.respond(commentsResponse)
        }catch (e : Exception){
            println(e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
        }
    }
    get("/deleteComment"){
        val token = call.request.headers["token"] ?: ""
        if (token.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid token")
            return@get
        }
         val commentId = call.queryParameters["commentId"] ?: ""
        if (commentId.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid Comment Id")
            return@get
        }
        try {
            val commentsUseCase by inject<commentsUseCase>()
            val response = commentsUseCase.deleteComment(commentId)
            call.respond(status = HttpStatusCode.OK , message = "Comment deleted")
        }catch (e : Exception){
            println("Exception in deleting comment is ${e.message}")
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
        }
    }
}