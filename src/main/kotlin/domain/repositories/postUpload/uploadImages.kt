package com.example.domain.repositories.postUpload

import com.example.data.models.ImageUrl
import com.example.data.models.VideoUrl

interface uploadPosts {
    suspend fun uploadImages(imagesArray : List<ByteArray> ,  userId : String) : List<String>

    suspend fun uploadVideo(video : ByteArray , userId: String) : String

    suspend fun deleteVideo(publicId : String)
}