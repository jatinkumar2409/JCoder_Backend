package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ReportDTO(
    val reportId : String,
    val contentType : String = "",
    val contentId : String = "",
    val reportBody : String = "",
    val reportedBy : String = "",
    val reportedAt : Long
)
