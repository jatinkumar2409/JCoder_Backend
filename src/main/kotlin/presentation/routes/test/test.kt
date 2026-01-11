package com.example.presentation.routes.test

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.test(){
    get("/"){
        call.respond(status = HttpStatusCode.OK , message = "Hello world")
    }
}