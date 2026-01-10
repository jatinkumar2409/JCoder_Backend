package com.example.data.impls.addUser

import com.example.data.models.User
import com.example.data.utils.generic.ServerExceptions
import com.example.domain.repositories.addUser.verifyUser
import com.google.firebase.auth.FirebaseAuth

class verifyUserImpl(private  val auth : FirebaseAuth) : verifyUser {
    override fun addUser(token : String): User {
       return try {
           val decoded = auth.verifyIdToken(token)
           User(
               name = decoded.name ,
               email = decoded.email,
               uid = decoded.uid ,
               profilePicture = decoded.picture
           )
       }catch (e : Exception){
           println("verify user expcetion : ${e.message} and ${e.cause}")
           throw ServerExceptions.UserNotVerifiedException()
       }
    }
}