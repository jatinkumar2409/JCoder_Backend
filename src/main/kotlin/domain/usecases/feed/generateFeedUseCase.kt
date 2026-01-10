package com.example.domain.usecases.feed

import com.example.data.helpers.createCacheKey
import com.example.data.helpers.toPosts
import com.example.data.models.FeedCursor
import com.example.data.models.ImageUrl
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.redis.FeedCache
import com.example.data.redis.RedisProvider
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document


class generateFeedUseCase(private val getFeed : buildFeedPipeline , private val db : MongoDatabase) {
     val feedCache = FeedCache(RedisProvider().commands)
     val posts = db.getCollection<Document>("posts")
    suspend fun getFeed(userId : String , cursor : FeedCursor?): List<PostDTO>{
        try {
            if (cursor == null){
                val cachedKey = createCacheKey(userId)
                val cachedPosts = feedCache.get(cachedKey)
                if (cachedPosts != null){
                    return cachedPosts
                }
            }
        val pipeline = getFeed.buildFeedPipeline(userId , cursor)
        val posts = posts.aggregate(pipeline).toList().toPosts()
            feedCache.put(cachedKey = createCacheKey(userId) , posts = posts)
        return posts
    }catch (e : Exception){
        println(e.message.toString())
        throw e
    }
    }
}


