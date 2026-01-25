package com.example.domain.usecases.report

import com.example.data.models.ReportDTO
import com.example.domain.repositories.report.submitReport

class submitReportUseCase(private val submitReportVal: submitReport) {
    suspend fun submitReport(reportDTO: ReportDTO){
        try {
            submitReportVal.submitReport(reportDTO)
        }catch (e : Exception){
            throw e
        }
    }
}