package com.example.presentation.plugins

import com.example.data.helpers.getEnv
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application


fun Application.setUpAdmin(){
//    val service = this::class.java.classLoader.getResourceAsStream("admin_sdk.json")
    val json = getEnv("FIREBASE_ADMIN")
    val service = json.byteInputStream()
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(service))
        .build()

    if (FirebaseApp.getApps().isEmpty()){
        FirebaseApp.initializeApp(options)
    }
}