package com.example.presentation.plugins

import com.example.domain.usecases.save.savePostUseCase
import com.example.presentation.routes.auth.authRoute
import com.example.presentation.routes.feed.generateFeed
import com.example.presentation.routes.likeAndComments.changeLikeAndCommentStatus
import com.example.presentation.routes.moderation.sightEngineWebhook
import com.example.presentation.routes.save.saveOrUnsavePost
import com.example.presentation.routes.search.generateSearchResults
import com.example.presentation.routes.test.test
import com.example.presentation.routes.upload.uploadPost
import com.example.presentation.routes.user.getUserPosts
import com.example.presentation.routes.user.userDetails
import com.example.presentation.routes.userUploads.getUserUploads
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRoutes(){
    routing {
        authRoute()
        uploadPost()
        sightEngineWebhook()
        userDetails()
        getUserPosts()
        generateFeed()
        changeLikeAndCommentStatus()
        generateSearchResults()
        saveOrUnsavePost()
        getUserUploads()
        test()
    }
}