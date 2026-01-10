package com.example.domain.repositories.homeFeed

import com.example.data.models.FeedCursor
import org.bson.Document

interface buildFeedPipeline{
    suspend fun buildFeedPipeline(userId : String , cursor : FeedCursor? , limit : Int = 12) : List<Document>
}