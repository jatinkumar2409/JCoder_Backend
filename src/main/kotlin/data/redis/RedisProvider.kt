package com.example.data.redis

import com.example.data.helpers.getEnv
import io.github.cdimascio.dotenv.dotenv
import io.lettuce.core.RedisClient

class RedisProvider {
    private val redisUrl = getEnv("REDIS_URL")
   private val client = RedisClient.create(redisUrl)
    private val connection = client.connect()
    val commands = connection.async()
}