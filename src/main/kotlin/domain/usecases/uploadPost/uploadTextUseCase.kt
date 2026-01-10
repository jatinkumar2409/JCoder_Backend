package com.example.domain.usecases.uploadPost

import com.example.data.models.Post
import com.example.data.models.User
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.google.firebase.database.core.UserWriteRecord
import java.util.UUID

class uploadTextUseCase(private val verifyText : sightEngine , private val uploadPostToMongo: uploadPostToMongo) {
    suspend fun uploadText(caption : String , tags : String , user : User , tagsList : List<String>){
        val verifyText = verifyText.verifyText(text = caption+tags)
        print(verifyText)
        val post = Post(
            postId = UUID.randomUUID().toString() ,
            userId = user.uid ,
            caption = caption ,
            tags = tagsList ,
            status = "public" ,
            contentType = "text" ,
            createdAt = System.currentTimeMillis()
        )
        uploadPostToMongo.uplaodPost(post)
        print(post)
    }
}