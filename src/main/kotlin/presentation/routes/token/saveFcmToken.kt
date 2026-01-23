package com.example.presentation.routes.token

import com.example.data.models.FcmDTO
import com.example.domain.usecases.token.addTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.saveFcmToken(){
   post("/saveFcmToken"){
       val fcmDto = call.receive<FcmDTO>()
       val addToken by inject<addTokenUseCase>()
       try {
           addToken.addFcmToken(fcmDto)
       }catch (e : Exception){
           println(e.message.toString())
           call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
       }
   }
}