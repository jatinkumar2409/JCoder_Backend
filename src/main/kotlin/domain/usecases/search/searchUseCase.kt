package com.example.domain.usecases.search

import com.example.data.models.FeedCursor
import com.example.data.models.FeedDTO
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.models.User
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import com.example.domain.repositories.search.getSearch
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class searchUseCase(private val getSearchVal: getSearch) {
    suspend fun searchPosts(query : String ,userId : String, cursor: FeedCursor?) : FeedDTO{
        try {
            val posts =  getSearchVal.searchPosts(query ,cursor , userId)
            val cursorPost = posts.lastOrNull()
            var cursor : FeedCursor? = null
            if (cursorPost != null) cursor = FeedCursor(score = cursorPost.score , createdAt = cursorPost.createdAt , postId = cursorPost.postId)
            return FeedDTO(posts , cursor)
        }catch (e : Exception){
            throw e
        }
    }
    suspend fun searchUsers(query: String) : List<User>{
        try {
            return getSearchVal.searchUsers(query)
        }catch (e : Exception){
            throw e
        }
    }
}