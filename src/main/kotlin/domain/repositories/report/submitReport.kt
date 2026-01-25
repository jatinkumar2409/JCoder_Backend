package com.example.domain.repositories.report

import com.example.data.models.ReportDTO

interface submitReport {
    suspend fun submitReport(reportDTO: ReportDTO)
}