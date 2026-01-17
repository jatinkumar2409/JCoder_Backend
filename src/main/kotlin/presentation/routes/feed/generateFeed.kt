package com.example.presentation.routes.feed

import com.example.data.models.FeedCursor
import com.example.data.models.FeedDTO
import com.example.data.models.RequestDTO
import com.example.domain.repositories.addUser.verifyUser
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import com.example.domain.usecases.feed.generateFeedUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.generateFeed(){
   post("/generateFeed"){
       val requestDTO = call.receive<RequestDTO>()
       val verifyUser by inject<verifyTokenUseCase>()
       val user = verifyUser.verifyToken(requestDTO.token)
       val generateFeed by inject<generateFeedUseCase>()
       val posts = generateFeed.getFeed(user.uid , requestDTO.feedCursor)
       val feedCursor = posts.lastOrNull()?.let {
           FeedCursor(
               score = it.score , createdAt = it.createdAt , postId = it.postId
           )
       }
       posts.map {
           println(it.isSaved)
       }

           call.respond(
               FeedDTO(
                   posts, feedCursor
               )
           )

      }
}

