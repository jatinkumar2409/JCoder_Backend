package com.example.data.helpers

import com.example.data.models.ImageUrl
import com.example.data.models.Post
import com.example.data.models.PostDTO
import org.bson.Document

 fun List<Document>.toPosts() : List<PostDTO>{
    return this.map { doc ->
         try {
             PostDTO(
                 postId = doc.getString("postId"),
                 userId = doc.getString("userId"),
                 caption = doc.getString("caption"),
                 contentType = doc.getString("contentType"),
                 imageUrls = doc.getList("imageUrls", Document::class.java)
                     ?.map { imgDoc ->
                         ImageUrl(
                             thumbnail = imgDoc.getString("thumbnail"),
                             main = imgDoc.getString("main"),
                             chat = imgDoc.getString("chat")
                         )
                     }
                     ?: emptyList(),
                 videoUrl = doc.getString("videoUrl"),
                 likes = doc.getInteger("likes", 0),
                 comments = doc.getInteger("comments", 0),
                 tags = doc.getList("tags", String::class.java) ?: emptyList(),
                 status = doc.getString("status"),
                 createdAt = doc.getLong("createdAt"),
                 expiredAt = doc.getLong("expiredAt"),
                 score = doc.getDouble("score"),
                 isLiked = doc.getBoolean("isLiked", false),
                 userName = doc.getString("userName"),
                 userProfile = doc.getString("userProfile"),
                 isSaved = doc.getBoolean("isSaved" , false)
             )
         }catch (e : Exception){
             println("\n\n\n\n\n Doc exception")
             println(doc.toJson())
             throw e
         }
        }

}
fun Post.toPostDto() : PostDTO{
    return PostDTO(
        postId = this.postId,
        userId = this.userId,
        caption = this.caption,
        contentType = this.contentType,
        imageUrls = this.imageUrls,
        videoUrl = this.videoUrl,
        likes = this.likes,
//        comments = this.comments!!,
        tags = this.tags,
        status = this.status,
        createdAt = this.createdAt,
        expiredAt = this.expiredAt,
        userName =  "",
        userProfile = "",
    )
}