package com.example.presentation.routes.search

import com.example.data.models.FeedDTO
import com.example.data.models.RequestDTO
import com.example.domain.usecases.search.searchUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.generateSearchResults(){
    post("/searchPosts"){
        print("Search posts is running")
        val query = call.queryParameters["query"] ?: ""
        val requestDTO = call.receive<RequestDTO>()
        if (query.trim().isEmpty() || requestDTO.token.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid params")
         return@post
        }
        try {
            val verifyUser by inject<verifyTokenUseCase>()
            val user = verifyUser.verifyToken(requestDTO.token)
            val searchUseCase by inject<searchUseCase>()
                val feedDto = searchUseCase.searchPosts(query ,user.uid , requestDTO.feedCursor)
                call.respond(feedDto)
                return@post
        }catch (e : Exception){
            print(e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
               return@post
        }
    }

    get("/searchUsers"){
        val query = call.queryParameters["query"] ?: ""
        if (query.trim().isEmpty()){
            call.respond(status = HttpStatusCode.BadRequest , message = "Invalid params")
            return@get
        }
        val searchUseCase by inject<searchUseCase>()
        try {
            val users = searchUseCase.searchUsers(query)
            call.respond(users)
        }catch (e : Exception){
            print(e.message.toString())
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
            return@get
        }

    }
}