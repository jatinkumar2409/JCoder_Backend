package com.example.data.helpers

fun createCacheKey(userId : String) : String{
    return "feed:first:$userId"
}