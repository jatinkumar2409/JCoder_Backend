package com.example.domain.repositories.postUpload

import java.io.File

interface sightEngine{
     suspend fun verifyPost(
         files : List<File> ,
         caption : String
     ) : Boolean

    suspend fun verifyVideo(
        video : File ,
        caption : String ,
        callbackUrl : String,
        userId : String
    ) : String

    suspend fun verifyText(
        text : String
    ) : Boolean
 }