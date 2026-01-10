package com.example.domain.repositories.likesAndComments

import com.example.data.models.Comment
import com.example.data.models.Like

interface likesAndComments {
    suspend fun likePost(like : Like) : Boolean
    suspend fun unLikePost(like : Like) : Boolean
    suspend fun loadComments(postId : String) : List<Comment>
    suspend fun addComment(comment: Comment) : Comment
    suspend fun deleteComment(commentId: String) : Boolean
}