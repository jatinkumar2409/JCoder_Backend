package com.example.presentation.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.minutes

fun Application.rateLimiter(){
    install(RateLimit){
        register(RateLimitName("default")) {
            rateLimiter(limit = 60, refillPeriod = 1.minutes)
        }
        register(RateLimitName("auth")) {
            rateLimiter(limit = 20, refillPeriod = 1.minutes)
        }
    }
}