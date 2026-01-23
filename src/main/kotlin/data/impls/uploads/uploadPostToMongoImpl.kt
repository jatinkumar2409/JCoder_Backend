package com.example.data.impls.uploads

import com.example.data.models.Post
import com.example.data.models.User
import com.example.data.utils.generic.retryOperation
import com.example.data.utils.generic.ServerExceptions
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.set
import com.mongodb.client.model.Updates.unset
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import org.bson.conversions.Bson

class uploadPostToMongoImpl(private val db : MongoDatabase) : uploadPostToMongo {
    val collection = db.getCollection<Post>("posts")
    val users = db.getCollection<User>("users")
    override suspend fun uplaodPost(
        post: Post
    ): Post {
        return retryOperation(exception = ServerExceptions.PostUploadAtDbException()) {
            val result = collection.insertOne(post)
           users.updateOne(Filters.eq("uid" , post.userId) , Updates.inc("postCount" ,1))
            print("The result for mongo db is " + result.wasAcknowledged())
            println(result.insertedId)
            post

        }
    }
    override suspend fun updatePost(postId: String, videoUrl: String ,status : String): Boolean {
        val updates = mutableListOf<Bson>()

        updates.add(set("videoUrl", videoUrl))
        updates.add(set("status", status))

        if (status == "public") {
            updates.add(unset("expiredAt"))
        }
      val result =  collection.updateOne(
           eq("postId",  postId) ,
          Updates.combine(
            updates
          )
        )
        return result.wasAcknowledged()
    }

    override suspend fun deletePost(postId: String , userId : String): Boolean {
        val result = collection.deleteOne(
            eq("postId" , postId)
        )
        users.updateOne(Filters.eq("uid" , userId) , Updates.inc("postCount" ,-1))

        return result.deletedCount == 1L
    }
}