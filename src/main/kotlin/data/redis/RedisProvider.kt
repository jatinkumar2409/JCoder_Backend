package com.example.data.redis

import io.github.cdimascio.dotenv.dotenv
import io.lettuce.core.RedisClient

class RedisProvider {
    private val dotenv = dotenv()
    private val redisUrl = dotenv.get("REDIS_URL")
   private val client = RedisClient.create(redisUrl)
    private val connection = client.connect()
    val commands = connection.async()
}