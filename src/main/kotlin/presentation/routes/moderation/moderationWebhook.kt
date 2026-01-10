package com.example.presentation.routes.moderation

import com.example.data.impls.uploads.flatten
import com.example.data.utils.uploadUtils.moderationChecker
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.example.domain.repositories.postUpload.uploadPosts
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.koin.ktor.ext.inject
import java.io.File

fun Route.sightEngineWebhook(){
    post("/sightengine/callback") {
        val tempFile = call.queryParameters["tempFile"]
        val token = call.queryParameters["token"]
        val userId = call.queryParameters["userId"] ?:""
        val postId = call.queryParameters["postId"] ?: ""
        val dotenv = dotenv()
        val envToken = dotenv["TOKEN"]
        if(token != envToken){
            return@post
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Invalid token")
        }
        val videoFile = File("temp/$tempFile")
        if(!videoFile.exists()){
            return@post
            call.respond(status = HttpStatusCode.ExpectationFailed , message = "Video file doesn't exist")
        }
        val payload = call.receiveText()
        val json = Json.parseToJsonElement(payload).jsonObject
        try {
            print("json payload : $json")
            val uploadPosts by inject<uploadPosts>()
            val checker by inject<moderationChecker>()
            val mongo by inject<uploadPostToMongo>()
            val scoreMaps = flatten(json, "" , checker)
            val result = checker.shouldBlock(scoreMaps)
            if (result) {
                val videoUrl = uploadPosts.uploadVideo(videoFile.readBytes(), userId)
                mongo.updatePost(postId, videoUrl , "public")
            } else {
                mongo.updatePost(postId, "" , "rejected")
                mongo.deletePost(postId)
            }
            call.respond(HttpStatusCode.OK, "Webhook run perfectly")
        }
        catch (e : Exception){
            print(e.message.toString())
        }
    }
}