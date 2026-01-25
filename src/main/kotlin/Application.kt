package com.example

import com.example.presentation.plugins.configureIndices
import com.example.presentation.plugins.configureRoutes
import com.example.presentation.plugins.contentNegotiation
import com.example.presentation.plugins.koin.koinSetUp
import com.example.presentation.plugins.rateLimiter
import com.example.presentation.plugins.setUpAdmin
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(){
  setUpAdmin()
  rateLimiter()
  configureIndices()
    koinSetUp()
    contentNegotiation()
    configureRoutes()

}


 