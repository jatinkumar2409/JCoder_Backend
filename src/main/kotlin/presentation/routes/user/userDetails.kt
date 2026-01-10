package com.example.presentation.routes.user


import com.example.domain.usecases.userDetails.getUserUseCase
import com.example.domain.usecases.userDetails.updateImageUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.koin.ktor.ext.inject
import java.io.File
import java.util.UUID

fun Route.userDetails() {
    get("/getUser") {
        print("get user is running")
        val uid = call.queryParameters["user_id"] ?: ""
        val getUser by inject<getUserUseCase>()
        val response = getUser.getUser(uid)
        if (response == null) {
            call.respond(status = HttpStatusCode.ExpectationFailed, "User not found")
            return@get
        } else {
            call.respond(response)
        }
    }
    post("/updateUser/image") {
        try {
            print("I am running")
            val verifyUser by inject<verifyTokenUseCase>()
            var token = ""
            val multipart = call.receiveMultipart()
            var imageFile: File? = null
            multipart.forEachPart { part ->
                println("RECEIVED PART → ${part::class}, name=${part.name}")
                when (part) {
                    is PartData.FileItem -> {
                        var fileName = "${UUID.randomUUID()}"
                        val tempDir = File("temp")
                        if (!tempDir.exists()) tempDir.mkdirs()
                       imageFile = File(tempDir , fileName)
                        val input = part.provider()
                        input.toInputStream().use { inputStream ->
                          imageFile.outputStream().buffered().use { output ->
                                inputStream.copyTo(output)
                            }
                        }
                    }
                    is PartData.FormItem -> {
                        when (part.name) {
                            "token" -> token = part.value
                        }
                    }

                    else -> Unit
                }
                part.dispose()
                }

                 val user = verifyUser.verifyToken(token)
                println("token is $token \n and image is$imageFile")
                if (imageFile == null || token.isEmpty()) {
                    call.respond(status = HttpStatusCode.BadRequest, message = "Something went wrong")
                    return@post
                }
                print("Image byte recieved")
                val uploadImage by inject<updateImageUseCase>()
                val response = uploadImage.updateImageUseCase(imageFile, user.uid)
                if (response.trim().isNotEmpty()) {
                    print("profileUpdated")
                    call.respond(status = HttpStatusCode.OK, response)
                } else {
                    print("failed to set up")
                    call.respond(status = HttpStatusCode.ExpectationFailed, "Something went wrong")
                }

        }catch (e : Exception){
            print("Exception is ${e.message}")
            call.respond(status = HttpStatusCode.ExpectationFailed , e.message.toString())
    }
    }
}