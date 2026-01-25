package com.example.data.impls.likesAndComments

import com.example.data.models.Comment
import com.example.data.models.Like
import com.example.data.models.Post
import com.example.data.models.Update
import com.example.data.models.User
import com.example.data.utils.generic.ServerExceptions
import com.example.data.utils.generic.updatesUtils
import com.example.data.utils.generic.userInfoService
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.example.domain.repositories.likesAndComments.likesAndComments
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.inc
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.bson.Document
import java.util.UUID

class likesAnsCommentsImpl(val db : MongoDatabase ,private val userInfo : userInfoService ,
    private val fcmService: fcmServiceRepo , private val fcmToken : fcmTokenRepo ,
    private val updatesUtils_: updatesUtils) : likesAndComments {
    val likes = db.getCollection<Like>("likes")
    val posts = db.getCollection<Post>("posts")
    val comments = db.getCollection<Comment>("comments")
    override suspend fun likePost(like: Like): Boolean {
        try {
            likes.insertOne(like)
            posts.updateOne(eq("postId" , like.postId) , inc("likes" , 1))
            return true
        }catch (e : Exception){
            throw e
        }
    }

    override suspend fun unLikePost(like: Like): Boolean {
        try {
            likes.deleteOne(Document().append("userId", like.userId).append("postId", like.postId))
            posts.updateOne(eq("postId" , like.postId) , inc("likes" , -1))
            return true
        }catch (e : Exception){
            throw e
        }
    }

    override suspend fun loadComments(postId: String): List<Comment> {
       try {
           val response = comments.find(eq("postId" , postId)).toList<Comment>()
           return response
       }catch (e : Exception){
           throw e
       }
    }

    override suspend fun addComment(comment: Comment): Comment {
     try {
         val response = comments.insertOne(comment)
         val ownerId = userInfo.getOwnerByPostId(comment.postId)
         if (ownerId != comment.userId){
             val userName = userInfo.getUserNameById(comment.userId)
             val tokens = fcmToken.getTokensByUser(ownerId)
             tokens.forEach {
                 fcmService.sendCommentNotification(commentByName = userName , token = it , postId = comment.postId)
             }
             val update =  Update(
                 id = UUID.randomUUID().toString() ,
                 title = "New Comment!",
                 text = "$userName commmented on your post",
                 createdAt = System.currentTimeMillis() ,
                 hasRead = false ,
                 userId = ownerId ,
                 type = "Comment" ,
                 navigationId = comment.postId
             )
             updatesUtils_.addUpdate(update)
         }
         posts.updateOne(eq("postId" , comment.postId) , inc("comments" , 1))

         return comment
     }catch (e : Exception){
         println("\n\nException at add comment is ${e.printStackTrace()}")
         throw e
     }
    }

    override suspend fun deleteComment(commentId: String): Boolean {
      try {
          val postId   = comments.find(eq("commentId" , commentId)).first().postId
          val response = comments.deleteOne(eq("commentId" , commentId))
          posts.updateOne(eq("postId" , postId) , inc("comments" , -1))
          return true
      }
      catch (e : Exception){
          throw e
      }
    }

}