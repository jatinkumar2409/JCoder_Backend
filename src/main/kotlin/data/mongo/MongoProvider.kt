package com.example.data.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient

class MongoProvider(val connectionString : String) {
    val client = MongoClient.create(
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .build()
    )
    val database = client.getDatabase("JCoderDB")
}