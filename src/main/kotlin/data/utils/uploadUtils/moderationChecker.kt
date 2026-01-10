package com.example.data.utils.uploadUtils

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlin.Double
import kotlin.String
import kotlin.collections.Map

class moderationChecker {
    val safeList = setOf("none" , "other" , "other_outdoor" , "outdoor_other" , "indoor_other" , "money" , "social" , "personal")

    fun shouldBlock(scores : Map<String , Double>) : Boolean{
      return scores.any{ (category , score ) ->
          val blocked = safeList.none { category.endsWith(it) } && score > 0.4
          if (blocked){
              print("\n Blocked by $category -> $score")
          }
          blocked
        }
    }
    fun isTextAllowed(jsonBody : JsonObject) : Boolean{
        try {
            val maxAllowed = 2
            jsonBody.values.forEach { value ->
                val matches = (value as? JsonObject)?.get("matches")?.jsonArray
                if ((matches?.size ?: 0) > maxAllowed) return false
            }
            return true
        }catch (e : Exception){
            print("Excpetion is checking : ${e.message}")
            return false
        }
    }
    fun isMediaTextAllowed(jsonBody: JsonObject) : Boolean {
       try {
           val maxAllowed = 3
           jsonBody.values.forEach { value ->
             val count = value.jsonArray
             return count.size <= maxAllowed
           }
       }
       catch (e : Exception){
           println("Excpetion at media text : ${e.message}")
       }
        return true
    }
}
