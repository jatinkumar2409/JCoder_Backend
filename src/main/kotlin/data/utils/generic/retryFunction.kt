package com.example.data.utils.generic

import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import io.ktor.client.plugins.HttpRequestTimeoutException

suspend fun <T> retryOperation(
    maxRetries: Int = 3,
    delayMs: Long = 1000,
    exception : ServerExceptions,
    operation: suspend () -> T
): T {
    var attempt = 0
    var currentDelay = delayMs

    while (true) {
        try {
            return operation()
        } catch (e: Exception) {
            attempt++
            val isRetryable = e is SocketTimeoutException ||
                    e is ConnectException ||
                    e is UnknownHostException ||
                    e is HttpRequestTimeoutException

            if (!isRetryable || attempt > maxRetries) {
                throw exception
            }

            println("Retryable exception: ${e.message}. Retrying in $currentDelay ms...")
            delay(currentDelay)
            currentDelay *= 2
        }
    }
}
