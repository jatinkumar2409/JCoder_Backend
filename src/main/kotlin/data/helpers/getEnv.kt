package com.example.data.helpers

import io.github.cdimascio.dotenv.dotenv

fun getEnv(key : String) : String{
    return try {
        dotenv().get(key)
    }catch (e : Exception){
        System.getenv(key)
    }
}