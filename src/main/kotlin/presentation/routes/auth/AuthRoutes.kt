package com.example.presentation.routes.auth


import com.example.data.utils.generic.ServerExceptions
import com.example.domain.repositories.addUser.addUser
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.authRoute(){
    val  verifyTokenUseCase by inject<verifyTokenUseCase>()
    val addUser by inject<addUser>()
    get("/") {
        call.respondText("Hello world")
    }
    post("/addUser") {
        try {
            val userToken = call.receive<Token>()
            val verifiedUser = verifyTokenUseCase.verifyToken(userToken.token)
                print("user is $verifiedUser")
                val add = addUser.addOrUpdateUser(verifiedUser)
                print("add is $add")
                if (add) call.respond(HttpStatusCode.OK, "User added")
                else call.respond(HttpStatusCode.Accepted, "User not added")

        }catch (e : ServerExceptions.UserNotVerifiedException){
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())
        }
        catch (e : Exception){
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())

        }
    }
}

@Serializable
data class Token(val token : String)