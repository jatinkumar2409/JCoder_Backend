package com.example.presentation.routes.updates

import com.example.domain.repositories.updates.isUpdatesUnread
import com.example.domain.repositories.updates.loadUpdates
import com.example.domain.usecases.updates.loadUpdatesUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.loadUpdates(){
    get("/getUpdates") {
        println("get updates is running")
        val userId = call.queryParameters["userId"] ?: run {
            println("User id is empty")
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty UserId")
            return@get
        }
        try {
        val laodUpdatesVal by inject<loadUpdatesUseCase>()
        val updates = laodUpdatesVal.loadUpdates(userId)
            println("Updates are ${updates.joinToString(",")}")
        call.respond(updates)
    }catch (e : Exception){
        println("Exception at getUpdates is ${e.message}")
        call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
    }
    }
}

fun Route.isUpdatesUnread(){
    get("/isUpdatesUnread"){
        println("isUpdatesUnread is running")
        val userId = call.queryParameters["userId"] ?: run {
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty UserId")
            return@get
        }
        val isUpdates by inject<isUpdatesUnread>()
         try{
                call.respond(isUpdates.isUpdatesUnread(userId))
         }catch (e : Exception){
             call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
         }
    }
    get("/setRead"){
        val userId = call.queryParameters["userId"] ?: run {
            call.respond(status = HttpStatusCode.BadRequest , message = "Empty UserId")
            return@get
        }
        val isUpdates by inject<isUpdatesUnread>()
         try {
             isUpdates.setRead(userId)
             call.respond(status = HttpStatusCode.OK , message = "Set Read Completed")
         }catch (e : Exception){
             call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")

         }
    }
}