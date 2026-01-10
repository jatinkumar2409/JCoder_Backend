package com.example.data.utils.generic

import io.ktor.client.plugins.ServerResponseException

sealed class ServerExceptions(
    val code: String,
    override val message: String?
) : RuntimeException(message){
    class UserNotVerifiedException(code: String = "USER_NOT_VERIFIED", message: String = "User not verified") : ServerExceptions(code , message)
    class ImageRejectedException(code: String = "IMAGE_REJECTED", message: String = "Image rejected in moderation") : ServerExceptions(code , message)
    class VideoRejectedException(code: String = "VIDEO_REJECTED", message: String = "Video rejected in moderation") : ServerExceptions(code , message)
    class PostRejectedException(code: String = "POST_REJECTED", message: String = "Post rejected in moderation") : ServerExceptions(code , message)
    class PostUploadException(code: String = "POST_UPLOAD_FAILED", message: String = "Post upload failed") : ServerExceptions(code , message)
    class PostUploadAtDbException(code: String = "POST_UPLOAD_AT_DB_FAILED", message: String = "Post upload failed") : ServerExceptions(code , message)
    class LikedStatusFailedException(code : String = "LIKE_STATUS_FAILED" , message: String = "Like status failed") : ServerExceptions(code , message)
}