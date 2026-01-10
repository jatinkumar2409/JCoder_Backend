package com.example.domain.usecases.likeAndComment

import com.example.data.models.Comment
import com.example.domain.repositories.likesAndComments.likesAndComments
import com.google.protobuf.Struct

class commentsUseCase(private val likesAndCommentsVal: likesAndComments) {

    suspend fun loadComments(postId : String) : List<Comment>{
        return try {
            likesAndCommentsVal.loadComments(postId)
        }catch (e : Exception){
            throw e
        }
    }
    
    suspend fun addComments(comment: Comment) : Comment{
        return try {
            likesAndCommentsVal.addComment(comment)
        }catch (e : Exception){
            throw e
        }
    }

    suspend fun deleteComment(commentId : String) : Boolean{
        return try {
            likesAndCommentsVal.deleteComment(commentId = commentId)
        }catch (e : Exception){
            throw e
        }
    }
}