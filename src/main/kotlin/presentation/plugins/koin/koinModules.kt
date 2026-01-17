package com.example.presentation.plugins.koin

import com.example.data.impls.getSearch.getSearchImpl
import com.example.data.impls.homeFeed.buildFeedImpl
import com.example.data.impls.likesAndComments.likesAnsCommentsImpl
import com.example.data.impls.save.savePostImpl
import com.example.data.impls.userDetails.userDetailsImpl
import com.example.data.impls.userUploads.userUploadsImpl
import com.example.data.utils.uploadUtils.uploadImage
import com.example.data.utils.user.updateUser
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import com.example.domain.repositories.likesAndComments.likesAndComments
import com.example.domain.repositories.save.saveOrUnsavePost
import com.example.domain.repositories.search.getSearch
import com.example.domain.repositories.userDetails.userDetails
import com.example.domain.repositories.userUploads.userUploads
import com.example.domain.usecases.feed.generateFeedUseCase
import com.example.domain.usecases.likeAndComment.commentsUseCase
import com.example.domain.usecases.likeAndComment.likePostUseCase
import com.example.domain.usecases.save.loadSavedPosts
import com.example.domain.usecases.save.savePostUseCase
import com.example.domain.usecases.search.searchUseCase
import com.example.domain.usecases.userDetails.getUserPostsUseCase
import com.example.domain.usecases.userDetails.getUserUseCase
import com.example.domain.usecases.userDetails.updateImageUseCase
import com.example.domain.usecases.userDetails.updateUserUseCase
import com.example.domain.usecases.userUploads.userUploadsUseCase
import org.koin.dsl.module

val genericModule = module {
    single<userDetails> {
        userDetailsImpl(get())
    }
    single {
        getUserUseCase(get())
    }
    single {
        uploadImage()
    }
    single {
        updateUser(get())
    }
    single {
        updateImageUseCase(get() , get() , get())
    }
    single {
        getUserPostsUseCase(get())
    }
    single<buildFeedPipeline>{
        buildFeedImpl()
    }
    single{
        generateFeedUseCase(get() , get())
    }
    single<likesAndComments> {
        likesAnsCommentsImpl(get())
    }
    single {
        likePostUseCase(get())
    }
    single{
        commentsUseCase(get())
    }
    single<getSearch> {
        getSearchImpl(get())
    }
    single{
        searchUseCase(get())
    }
    single<saveOrUnsavePost> {
        savePostImpl(get())
    }
    single {
        savePostUseCase(get())
    }
    single {
        updateUserUseCase(get())
    }
    single{
        loadSavedPosts(get())
    }
    single<userUploads> {
        userUploadsImpl(get())
    }
    single{
        userUploadsUseCase(get())
    }
}