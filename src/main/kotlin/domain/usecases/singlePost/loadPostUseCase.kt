package com.example.domain.usecases.singlePost

import com.example.data.models.PostDTO
import com.example.domain.repositories.singlePost.loadSinglePost

class loadPostUseCase(private val loadSinglePostVal: loadSinglePost) {
    suspend fun loadPost(postId : String ,userId : String) : PostDTO{
        return loadSinglePostVal.loadPostFromDB(postId , userId)
    }
}