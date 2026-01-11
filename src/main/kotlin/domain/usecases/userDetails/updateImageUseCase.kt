package com.example.domain.usecases.userDetails

import com.cloudinary.Cloudinary
import com.example.data.helpers.getEnv
import com.example.data.impls.uploads.sightEngineImpl
import com.example.data.utils.uploadUtils.uploadImage
import com.example.data.utils.user.updateUser
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPosts
import io.github.cdimascio.dotenv.dotenv
import io.ktor.util.valuesOf
import java.io.File

class updateImageUseCase(private val verifyPost: sightEngine , private val uploadProfile: uploadImage  ,
    private val update : updateUser
    ) {
    val cloud = getEnv("CLOUD_NAME")
    val apiKey = getEnv("CLOUD_API")
    val apiSecret = getEnv("CLOUD_SECRET")
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to cloud,
            "api_key" to apiKey ,
            "api_secret" to apiSecret,
            "timeout" to 60_000,
            "connection_timeout" to 15_000

        )
    )
    suspend fun updateImageUseCase(imageFile : File ,userId : String) : String{
        print("update image is running")
        val imageByte = imageFile.readBytes()
        try {
            println("update image is running and byte array is $imageByte")
            val verifyPost = verifyPost.verifyPost(listOf(imageFile), "")
            if (verifyPost) {
                println("The content response is $verifyPost")
                val uploadImage = uploadProfile.provideProfileImage(cloudinary, imageByte, userId)
                print("Image url is $uploadImage")
                if (uploadImage.isNotEmpty()) {
                    val updateUser = update.updateOneField("profilePicture", uploadImage, userId)
                    if (updateUser) {
                        print("updating user in db")
                        return uploadImage
                    }
                }
            }
        }catch (e : Exception){
            throw e
            print("Exception is ${e.message}")
        }
        return ""
    }

}