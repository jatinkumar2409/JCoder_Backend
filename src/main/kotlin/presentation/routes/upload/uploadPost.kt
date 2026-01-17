package com.example.presentation.routes.upload

import com.example.data.utils.generic.ServerExceptions
import com.example.domain.usecases.uploadPost.uploadImageUseCase
import com.example.domain.usecases.uploadPost.uploadTextUseCase
import com.example.domain.usecases.uploadPost.uploadVideoUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.koin.ktor.ext.inject
import java.io.File
import java.util.UUID

fun Route.uploadPost(){
    post("/uploadPost"){
        print("I am running")
        val multipart = call.receiveMultipart()
      var token = ""
      var caption = ""
      var tags = ""
        var contentType = ""
        val tempDir = File("temp")
        if(!tempDir.exists()) tempDir.mkdirs()
      val files = mutableListOf<File>()
        multipart.forEachPart { part ->
            when(part){
                is PartData.BinaryChannelItem -> TODO()
                is PartData.BinaryItem -> TODO()
                is PartData.FileItem ->{
                     var fileName = "${UUID.randomUUID()}"
                    val file = File(tempDir , fileName)
                    val input = part.provider()
   input.toInputStream().use { inputStream ->
       file.outputStream().buffered().use { output ->
           inputStream.copyTo(output)
       }
   }
       files.add(file)
                }
                is PartData.FormItem ->{
                    when(part.name){
                        "caption" -> caption = part.value
                        "tags" -> tags = part.value
                        "token" -> token = part.value
                        "contentType" -> contentType = part.value
                    }
                }
            }

        }
        if(contentType == "image" || contentType == "video"){
            if(files.isEmpty()) call.respond(HttpStatusCode.ExpectationFailed , "Invalid or missing details")
        }
        if(caption.trim().isEmpty() || tags.trim().isEmpty() || token.trim().isEmpty()){
            call.respond(HttpStatusCode.ExpectationFailed , "Invalid or missing details")
        }
        try {
            val verifyUser by inject<verifyTokenUseCase>()
            val user = verifyUser.verifyToken(token)
            val tagsList = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if(contentType == "image") {
                val uploadImage by inject<uploadImageUseCase>()
                 uploadImage.uploadImage(files , caption , user , tags , tagsList)
                call.respond(HttpStatusCode.Accepted, "Post Uploaded Successfully")
            }
            else if(contentType == "video"){
               val uploadVideo by inject<uploadVideoUseCase>()
                uploadVideo.uploadVideo(files.first() , caption  ,user, tags , tagsList)
                call.respond(HttpStatusCode.Accepted, "Post Uploaded Successfully")
            }
            else if(contentType == "text"){
               val uploadText by inject<uploadTextUseCase>()
                uploadText.uploadText(caption , tags , user , tagsList)
                call.respond(status = HttpStatusCode.OK , message = "Post Uploaded Successfully")
            }
            else{
                call.respond(status = HttpStatusCode.BadRequest , message = "Invalid content type")
            }
        } catch (e : ServerExceptions.UserNotVerifiedException){
            println("Exception at uploading the post is ${e.message}")
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())
        }
        catch (e : ServerExceptions.ImageRejectedException){
            println("Exception at uploading the post is ${e.message}")
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())
        }
        catch (e: ServerExceptions.PostUploadException){
            println("Exception at uploading the post is ${e.message}")
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())

        }
        catch (e : ServerExceptions.PostUploadAtDbException){
            println("Exception at uploading the post is ${e.message}")
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())

        }
        catch (e : ServerExceptions.PostRejectedException){
            println("Exception at uploading the post is ${e.message}")
            call.respond(HttpStatusCode.ExpectationFailed , e.message.toString())
        }
        catch (e : Exception){
            println("Exception at uploading the post is ${e.message}")

            call.respond(HttpStatusCode.ExpectationFailed , "Something went wrong")
        }
    }
}