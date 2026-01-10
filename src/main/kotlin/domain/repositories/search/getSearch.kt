package com.example.domain.repositories.search

import com.example.data.models.FeedCursor
import com.example.data.models.Post
import com.example.data.models.PostDTO
import com.example.data.models.User
import org.bson.Document

interface getSearch {
    suspend fun searchPosts(query: String , cursor: FeedCursor? , userId : String) : List<PostDTO>
    suspend fun searchUsers(query: String) : List<User>
}