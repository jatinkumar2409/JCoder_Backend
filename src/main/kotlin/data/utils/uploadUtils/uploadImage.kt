package com.example.data.utils.uploadUtils

import com.cloudinary.Cloudinary
import com.cloudinary.Transformation
import com.cloudinary.utils.ObjectUtils
import com.example.data.models.ImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.data.utils.generic.ServerExceptions
import com.example.data.utils.generic.retryOperation
import java.util.UUID

class uploadImage {
    suspend fun uplaodImage(cloudinary: Cloudinary , imageFile : ByteArray , userId : String ,index : Int) : ImageUrl = withContext(Dispatchers.IO) {

      try {
          print("upload image is running")
          val publicId = "posts/images/$userId/${UUID.randomUUID()}_$index"
          val resultId = cloudinary.uploader().upload(
              imageFile, ObjectUtils.asMap(
                  "public_id", publicId,
                  "resource_type", "image"
              )
          )
          val uplaodPublicId = resultId["public_id"].toString()
          println("public id $uplaodPublicId")
          val thumbnail = cloudinary.url().transformation(
              Transformation<Transformation<*>>()
                  .width(1440)
                  .crop("scale")
                  .fetchFormat("webp")
                  .quality("40")
          )
              .generate(uplaodPublicId)
          val main = cloudinary.url().transformation(
              Transformation<Transformation<*>>()
                  .width(1440)
                  .crop("scale")
                  .fetchFormat("webp")
                  .quality("70")
          )
              .generate(uplaodPublicId)
          val chat = cloudinary.url().transformation(
              Transformation<Transformation<*>>()
                  .width(300)
                  .crop("scale")
                  .fetchFormat("webp")
                  .quality("30")
          )
              .generate(uplaodPublicId)

          print("thumbnail is $thumbnail")
          ImageUrl(
              thumbnail = thumbnail,
              main = main,
              chat = chat
          )

      }catch (e : Exception){
          print("Upload failed ${e.message}")
          e.printStackTrace()
          throw ServerExceptions.PostUploadException()
      }
    }
   suspend fun provideProfileImage(cloudinary : Cloudinary, imageFile : ByteArray, userId : String) : String{
        return retryOperation(exception =ServerExceptions.PostUploadException()) {
            val publicId = "profile/$userId/${UUID.randomUUID()}"
            val resultId = cloudinary.uploader().upload(
                imageFile, ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "image"
                )
            )
            val uploadPublicId = resultId["public_id"].toString()
            cloudinary.url().transformation(
                Transformation<Transformation<*>>()
                    .width(180)
                    .crop("scale")
                    .fetchFormat("webp")
                    .quality("30")
            )
                .generate(uploadPublicId)
        }
    }
}