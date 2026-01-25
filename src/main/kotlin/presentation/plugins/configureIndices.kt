package com.example.presentation.plugins

import com.example.data.models.Comment
import com.example.data.models.FcmToken
import com.example.data.models.Follow
import com.example.data.models.Like
import com.example.data.models.Post
import com.example.data.models.Update
import com.example.data.models.User
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarting
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.koin.ktor.ext.inject
import java.util.concurrent.TimeUnit

fun Application.configureIndices(){
      val db by inject<MongoDatabase>()
     environment.monitor.subscribe(ApplicationStarting) {
         runBlocking {
             val users = db.getCollection<User>("users")
             val posts = db.getCollection<Post>("posts")
             val follows = db.getCollection<Follow>("follows")
             val likes = db.getCollection<Like>("Likes")
             val comments = db.getCollection<Comment>("comments")
             val fcm_tokens = db.getCollection<FcmToken>("fcm_tokens")
             val updates = db.getCollection<Update>("updates")
             users.createIndex(Document("uid", 1), IndexOptions().unique(true))
             users.createIndex(Indexes.ascending("followers"))
             posts.createIndex(Indexes.ascending("expiredAt") , IndexOptions().expireAfter(
                 0 , TimeUnit.SECONDS
             ))

             posts.createIndex(
                 Indexes.compoundIndex(
                     Indexes.ascending("status") ,
                     Indexes.descending("createdAt") ,
                     Indexes.descending("postId") ,

                 )
             )


             follows.createIndex(
                 Indexes.compoundIndex(
                     Indexes.ascending("followerId"),
                     Indexes.ascending("followingId")
                 ),
                 IndexOptions().unique(true)
             )

             // Likes
             likes.createIndex(
                 Indexes.compoundIndex(
                     Indexes.ascending("userId"),
                     Indexes.ascending("postId")
                 ),
                 IndexOptions().unique(true)
             )

             comments.createIndex(
                 Indexes.descending("createdAt"),
                 IndexOptions().unique(true)
             )

             fcm_tokens.createIndex(
                 Indexes.descending("lastUsed") , IndexOptions().expireAfter(
                     7776000 , TimeUnit.SECONDS
                 )
             )
             updates.createIndex(
                 Indexes.descending("createdAt") , IndexOptions().expireAfter(
                     864000 , TimeUnit.SECONDS
                 )
             )
         }
     }
}