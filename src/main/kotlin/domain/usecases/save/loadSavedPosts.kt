package com.example.domain.usecases.save

import com.example.data.models.PostDTO
import com.example.domain.repositories.save.saveOrUnsavePost

class loadSavedPosts(private val saveOrUnsavePostVal: saveOrUnsavePost) {
    suspend fun loadSavedPosts(userId : String , page : Int , limit : Int) : List<PostDTO>{
        try {
           return saveOrUnsavePostVal.loadSavedPosts(userId , page , limit)
        }catch (e : Exception){
            println(e.printStackTrace())
            throw e
        }
    }
}