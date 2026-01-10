package com.example.data.impls.uploads

import com.example.data.utils.uploadUtils.moderationChecker
import com.example.data.utils.generic.retryOperation
import com.example.data.utils.generic.ServerExceptions
import com.example.domain.repositories.postUpload.sightEngine
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject


import java.io.File
import java.util.UUID

class sightEngineImpl(private val checker : moderationChecker , private val client : HttpClient) : sightEngine {
    val dotenv = dotenv()
    val apiUser = dotenv.get("SIGHT_ENGINE_USER")
    val apiSecret = dotenv.get("SIGHT_ENGINE_SECRET")
    override suspend fun verifyPost(
        files: List<File>,
        caption: String
    ): Boolean {
        return retryOperation(exception = ServerExceptions.PostUploadException()) {
            val response = client.submitFormWithBinaryData(
                url = "https://api.sightengine.com/1.0/check.json",
                formData = formData {
                    append(
                        "models",
                        "nudity-2.1,weapon,alcohol,recreational_drug,medical,offensive-2.0,text-content,gore-2.0,text,qr-content,tobacco,violence,self-harm,gambling"
                    )
                    append("api_user", apiUser)
                    append("api_secret", apiSecret)
                    append("text", caption)
                    files.forEach { file ->
                        append(
                            key = "media[]",
                            value = file.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                                append(HttpHeaders.ContentType, "image/jpeg")
                            }

                        )
                    }
                }
            )
            if (!verifyText(caption)) throw ServerExceptions.PostRejectedException()
            val responseBody = response.bodyAsText()
            val scoreMaps = flatten(Json.parseToJsonElement(responseBody).jsonObject, "", checker)
            val checker = checker.shouldBlock(scoreMaps)
            print("Verified , ${responseBody}")
           !checker
        }

    }

    override suspend fun verifyVideo(video : File, caption: String , callbackUrl : String , userId : String) : String{
      return retryOperation(exception = ServerExceptions.PostRejectedException()) {
            print("apiUser is $apiUser and api secret is $apiSecret")
                val postId = UUID.randomUUID().toString()
                val response = client.submitFormWithBinaryData(
                    url = "https://api.sightengine.com/1.0/video/check.json", formData = formData {
                        append(
                            "models",
                            "nudity-2.1,weapon,alcohol,recreational_drug,medical,offensive-2.0,text-content,gore-2.0,text,qr-content,tobacco,violence,self-harm,gambling"
                        )
                        append("api_user", apiUser)
                        append("api_secret", apiSecret)
                        append("text", caption)
                        append(
                            "callback_url",
                            "$callbackUrl?tempFile=${video.name}&token=osidokkwhdoeihdlsklssl&postId=$postId&userId=$userId"
                        )
                        append(key = "media", value = video.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "video/mp4")
                            append(HttpHeaders.ContentDisposition, "filename=${video.name}")
                        })
                    }
                )
                if (!verifyText(caption)) throw ServerExceptions.PostRejectedException()
                println("HTTP status: ${response.status}")
                println("Response body: ${response.bodyAsText()}")
              postId
        }
        }



    override suspend fun verifyText(text: String): Boolean {
       return retryOperation(exception = ServerExceptions.PostUploadException()) {
           val response = client.submitFormWithBinaryData(
               url = "https://api.sightengine.com/1.0/text/check.json", formData = formData {
                   append("text", text)
                   append("lang", "en")
                   append("mode", "standard")
                   append(
                       "models",
                       "profanity,personal,link,drug,weapon,spam,content-trade,money-transaction,extremism,violence,self-harm,medical"
                   )
                   append("api_user", apiUser)
                   append("api_secret", apiSecret)
               }
           )
           println("The response for text moderation is ${response.bodyAsText()}")
           val check = checker.isTextAllowed(response.body())
           if (!check) {
               throw ServerExceptions.PostRejectedException()
           }
           true
       }
    }

}
   val ignorePropsList = listOf("request")
fun flatten(json : JsonObject , prefix : String ,checker: moderationChecker) : Map<String , Double> =
     json.flatMap { (key , value) ->
         when{
             key in ignorePropsList -> listOf()
             key == "text" -> {
                 val textJson = value as? JsonObject ?: return@flatMap emptyList()
                 if(!checker.isMediaTextAllowed(textJson)) throw ServerExceptions.PostRejectedException()
                 else print(textJson.toString())
                emptyList()
             }
           value is JsonObject -> flatten(value , "$prefix$key." , checker).toList()
             value is JsonPrimitive && value.doubleOrNull != null -> listOf("$prefix$key" to value.double)
             else -> emptyList()
         }

     }.toMap()
