package com.example.data.redis

import com.example.data.models.FeedDTO
import com.example.data.models.PostDTO
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json

class FeedCache(private val redis : RedisAsyncCommands<String , String>) {

    suspend fun get(cachedKey : String) : List<PostDTO>?{
        return try {
            val cached = redis.get(cachedKey).await()
            if (cached == null){
                null
            }
           else{
                Json.decodeFromString<List<PostDTO>>(cached)
            }
        }catch (e : Exception){
            null
        }
    }

    suspend fun put(cachedKey: String, posts : List<PostDTO>, ttl : Long = 60){
        val json = Json.encodeToString(posts)
        try {
            redis.setex(cachedKey , ttl , json).await()
        }
        catch (e : Exception){
            print(e.message.toString())
        }
    }

    suspend fun invalidate(cachedKey : String){
        try {
            redis.del(cachedKey).await()
        }catch (e : Exception){
            print(e.message.toString())
        }
    }
}