package com.example.presentation.plugins.koin

import com.example.data.impls.fcmToken.fcmServiceImpl
import com.example.data.impls.fcmToken.fcmTokenImpl
import com.example.data.impls.follow.followUserImpl
import com.example.data.impls.getSearch.getSearchImpl
import com.example.data.impls.homeFeed.buildFeedImpl
import com.example.data.impls.likesAndComments.likesAnsCommentsImpl
import com.example.data.impls.report.submitReportImpl
import com.example.data.impls.save.savePostImpl
import com.example.data.impls.updates.loadUpdatesImpl
import com.example.data.impls.userDetails.userDetailsImpl
import com.example.data.impls.userUploads.userUploadsImpl
import com.example.data.utils.generic.mongoUtils
import com.example.data.utils.generic.updatesUtils
import com.example.data.utils.generic.userInfoService
import com.example.data.utils.generic.webhookUtils
import com.example.data.utils.uploadUtils.uploadImage
import com.example.data.utils.user.updateUser
import com.example.domain.repositories.fcmToken.fcmServiceRepo
import com.example.domain.repositories.fcmToken.fcmTokenRepo
import com.example.domain.repositories.follow.followUserRepo
import com.example.domain.repositories.homeFeed.buildFeedPipeline
import com.example.domain.repositories.likesAndComments.likesAndComments
import com.example.domain.repositories.report.submitReport
import com.example.domain.repositories.save.saveOrUnsavePost
import com.example.domain.repositories.search.getSearch
import com.example.domain.repositories.updates.isUpdatesUnread
import com.example.domain.repositories.updates.loadUpdates
import com.example.domain.repositories.userDetails.userDetails
import com.example.domain.repositories.userUploads.userUploads
import com.example.domain.usecases.feed.generateFeedUseCase
import com.example.domain.usecases.follow.followUserUseCase
import com.example.domain.usecases.likeAndComment.commentsUseCase
import com.example.domain.usecases.likeAndComment.likePostUseCase
import com.example.domain.usecases.report.submitReportUseCase
import com.example.domain.usecases.save.loadSavedPosts
import com.example.domain.usecases.save.savePostUseCase
import com.example.domain.usecases.search.searchUseCase
import com.example.domain.usecases.token.addTokenUseCase
import com.example.domain.usecases.updates.loadUpdatesUseCase
import com.example.domain.usecases.userDetails.deletePostUseCase
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
        likesAnsCommentsImpl(get() , get() , get() , get() , get())
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
    single{
        userInfoService(get())
    }
    single<fcmTokenRepo>{
        fcmTokenImpl(get())
    }
    single<fcmServiceRepo>{
        fcmServiceImpl(get())
    }
    single {
        addTokenUseCase(get())
    }
    single<followUserRepo> {
        followUserImpl(get() , get() , get() , get() , get())
    }
    single{
        followUserUseCase(get())
    }
    single{
        webhookUtils(get() , get() , get() , get())
    }
    single<loadUpdates>{
        loadUpdatesImpl(get())
    }
    single {
        loadUpdatesUseCase(get())
    }
    single {
        isUpdatesUnread(get())
    }
    single{
        updatesUtils(get())
    }
    single{
        mongoUtils(get())
    }
    single{
        deletePostUseCase(get() , get())
    }
    single<submitReport>{
        submitReportImpl(get())
    }
    single{
        submitReportUseCase(get())
    }
}