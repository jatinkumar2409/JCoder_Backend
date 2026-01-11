package com.example.data.impls.uploads


import com.cloudinary.Cloudinary
import com.cloudinary.Transformation
import com.cloudinary.utils.ObjectUtils
import com.example.data.helpers.getEnv
import com.example.data.models.ImageUrl
import com.example.data.utils.generic.retryOperation
import com.example.data.utils.generic.ServerExceptions
import com.example.data.utils.uploadUtils.uploadImage
import com.example.domain.repositories.postUpload.uploadPosts
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.UUID

class uploadPostsImpl(private val upload : uploadImage) : uploadPosts {
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
    override suspend fun uploadImages(imagesBytes : List<ByteArray> , userId : String): List<ImageUrl>  {
        return retryOperation(exception = ServerExceptions.PostUploadException()) {
          coroutineScope {
              print("cloud is running")
              imagesBytes.mapIndexed { index, image ->
                  async(Dispatchers.IO) {
                      upload.uplaodImage(cloudinary ,image, userId, index)
                  }
              }.awaitAll()
          }
        }
    }

    override suspend fun uploadVideo(video: ByteArray, userId: String): String {
        return retryOperation(exception = ServerExceptions.PostUploadException()) {
            val publicId = "posts/videos/$userId/${UUID.randomUUID()}"
            val resultId = cloudinary.uploader().upload(
                video, ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "video"
                )
            )
            val uploadPublicId = resultId["public_id"].toString()

            val url = cloudinary.url()
                .resourcType("video")
                .transformation(
                    Transformation<Transformation<*>>()
                        .quality("auto")
                )
                .generate(uploadPublicId)
            print("videoUrl is $url  \n")
             url
        }
    }

    override suspend fun deleteVideo(publicId: String) {
        try {
            cloudinary.uploader().destroy(
                publicId, ObjectUtils.asMap(
                    "resource_type", "video",
                    "invalidate", true
                )
            )
        }catch (e : Exception){
            throw e
        }
    }
}
