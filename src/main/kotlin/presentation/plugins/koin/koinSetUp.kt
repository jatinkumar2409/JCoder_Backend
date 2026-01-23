package com.example.presentation.plugins.koin

import com.example.data.helpers.getEnv
import com.example.data.impls.addUser.addUserImpl
import com.example.data.impls.uploads.sightEngineImpl
import com.example.data.impls.uploads.uploadPostsImpl
import com.example.data.impls.uploads.uploadPostToMongoImpl
import com.example.data.impls.addUser.verifyUserImpl
import com.example.data.impls.singlePost.loadSinglePostImpl
import com.example.data.mongo.MongoProvider
import com.example.data.utils.uploadUtils.moderationChecker
import com.example.domain.repositories.addUser.addUser
import com.example.domain.repositories.postUpload.sightEngine
import com.example.domain.repositories.postUpload.uploadPosts
import com.example.domain.repositories.postUpload.uploadPostToMongo
import com.example.domain.repositories.addUser.verifyUser
import com.example.domain.repositories.singlePost.loadSinglePost
import com.example.domain.usecases.singlePost.loadPostUseCase
import com.example.domain.usecases.uploadPost.uploadImageUseCase
import com.example.domain.usecases.uploadPost.uploadTextUseCase
import com.example.domain.usecases.uploadPost.uploadVideoUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import com.example.presentation.plugins.contentNegotiation
import com.google.firebase.auth.FirebaseAuth
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.koinSetUp(){
    val mongoUri = getEnv("MONGO_URI")
    val pluginModule = module {
       single {
           FirebaseAuth.getInstance()
       }
        single {
            moderationChecker()
        }
        single {
            mongoUri
        }
        single<MongoDatabase> {
            MongoProvider(mongoUri).database
        }
         single<uploadPosts> {
             uploadPostsImpl(get())
         }
     single<verifyUser> {
         verifyUserImpl(get())
     }
        single<uploadPostToMongo> {
            uploadPostToMongoImpl(get())
        }
        single<addUser> {
            addUserImpl(get())
        }
        single<verifyTokenUseCase> {
            verifyTokenUseCase(get())
        }
        single<sightEngine>{
            sightEngineImpl(get() , get())
        }
     single<uploadImageUseCase> {
         uploadImageUseCase(get() , get() , get() , get() , get() , get() , get())
     }
        single<uploadVideoUseCase> {
            uploadVideoUseCase(get() , get() , get())
        }
        single<uploadTextUseCase> {
            uploadTextUseCase(get() , get() , get() , get() , get() ,get())
        }
        single {
            HttpClient(CIO){
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation){
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
                install(HttpTimeout){
                    connectTimeoutMillis = 10_000
                    requestTimeoutMillis = 120_000
                    socketTimeoutMillis = 120_000
                }

            }
        }
        single<loadSinglePost> {
            loadSinglePostImpl(get())
        }
        single {
            loadPostUseCase(get())
        }
    }

    install(Koin){
        modules(
      pluginModule , genericModule
        )
    }
}