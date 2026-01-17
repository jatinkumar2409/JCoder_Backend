package com.example.data.impls.homeFeed

import com.example.data.models.FeedCursor
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import org.bson.Document
import java.util.Date

class buildFeedImpl : buildFeedPipeline {
    override suspend fun buildFeedPipeline(
        userId: String,
        cursor: FeedCursor?,
        limit: Int
    ): List<Document> {
        val pipeline = mutableListOf<Document>()
        pipeline += Document("\$match", Document(
            "status", "public"
        ))
        pipeline += Document( "\$lookup" , Document()
            .append("from" , "follows")
            .append("let" , Document("postUserId" , "\$userId"))
            .append("pipeline" , listOf(
                Document("\$match" , Document("\$expr" , Document("\$and" ,  listOf(
                    Document("\$eq" , listOf("\$followingId" , "\$\$postUserId")) ,
                    Document("\$eq", listOf("\$followerId", userId))
                ))))
            ))
            .append("as" , "followInfo")
        )
        pipeline += Document("\$lookup" , Document()
            .append("from" , "likes")
            .append("let" , Document("postId" , "\$postId"))
            .append("pipeline" , listOf(
                Document("\$match" , Document("\$expr" , Document("\$and" ,
                    listOf(
                        Document("\$eq" , listOf("\$postId" , "\$\$postId")) ,
                        Document("\$eq", listOf("\$userId", userId))
                    )))) ,
                Document("\$limit" , 1)
            )).append("as" , "likeInfo")
        )

        pipeline += Document("\$lookup" , Document()
            .append("from" , "saved")
            .append("let" , Document("postId" , "\$postId"))
            .append("pipeline" , listOf(
                Document("\$match" , Document("\$expr" , Document("\$and" ,
                    listOf(
                        Document("\$eq" , listOf("\$postId" , "\$\$postId")) ,
                        Document("\$eq", listOf("\$userId", userId))
                    )))) ,
                Document("\$limit" , 1)
            )).append("as" , "saveInfo")
            )
        pipeline += Document("\$lookup" , Document()
            .append("from" , "users")
            .append("let" , Document("postUserId" , "\$userId"))
            .append("pipeline" , listOf(
                Document("\$match" , Document("\$expr" , Document("\$eq" , listOf("\$uid" , "\$\$postUserId")))) ,
                Document("\$limit" ,1)
            )).append("as" , "userInfo")
            )

        pipeline += Document("\$addFields" , Document()
            .append("userName" , Document("\$arrayElemAt" , listOf("\$userInfo.name" , 0)))
            .append("userProfile" , Document("\$arrayElemAt" , listOf("\$userInfo.profilePicture" , 0))))
        pipeline += Document("\$addFields", Document()
            .append("followBonus",
                Document("\$cond", listOf(
                    Document("\$gt", listOf(Document("\$size", "\$followInfo"), 0)),
                    2,
                    0
                ))
            ).append("isSaved" , Document("\$gt" , listOf(
                Document("\$size" , "\$saveInfo") , 0
            )))
            .append("isLiked",
                Document("\$gt", listOf(
                    Document("\$size", "\$likeInfo"),
                    0
                )))
        )
        pipeline += Document("\$addFields", Document("ageHours",
            Document("\$divide", listOf(
                Document("\$toDouble",
                    Document("\$subtract", listOf(
                        "\$\$NOW",
                        "\$createdAt"
                    ))
                ) ,
                3600000.0
            ))
        ))
        pipeline += Document("\$addFields", Document("score",
            Document("\$subtract", listOf(
                Document("\$add", listOf(Document("\$ifNull", listOf("\$likes", 0)),
                    Document("\$ifNull", listOf("\$comments", 0)), "\$followBonus")),
                Document("\$multiply", listOf("\$ageHours", 0.2))
            ))
        ))
         if(cursor != null){
             pipeline += Document("\$match" , Document("\$or" , listOf(
                 Document("score" , Document("\$lt",  cursor.score)) ,
                 Document("\$and" , listOf(
                     Document("score" , Document("\$eq" , cursor.score)) ,
                     Document("createdAt" , Document("\$lt" , cursor.createdAt))

                 )),
                 Document("\$and" , listOf(
                     Document("score" , Document("\$eq" , cursor.score)) ,
                     Document("createdAt" , Document("\$eq"  , cursor.createdAt)) ,
                     Document("postId" , Document("\$lt" , cursor.postId))
                 ))
             )))
         }
        pipeline += Document("\$group", Document()
            .append("_id", "\$postId")
            .append("doc", Document("\$first", "\$\$ROOT"))
        )
        pipeline += Document("\$replaceRoot", Document("newRoot", "\$doc"))

        pipeline += Document("\$sort", Document("score", -1).append("createdAt", -1).append("postId", -1))

        // 7️⃣ Limit
        pipeline += Document("\$limit", limit)

        // 8️⃣ Clean output
        pipeline += Document("\$project", Document()
            .append("postId", 1)
            .append("userId", 1)
            .append("caption", 1)
            .append("contentType", 1)
            .append("imageUrls", 1)
            .append("videoUrl", 1)
            .append("likes", 1)
            .append("comments", 1)
            .append("tags", 1)
            .append("status", 1)
            .append("createdAt", 1)
            .append("expiredAt", 1)
            .append("score", 1)
            .append("isLiked", 1)
            .append("userName" , 1)
            .append("userProfile" , 1)
            .append("isSaved" , 1)
        )

        return pipeline
    }
}