package com.example.data.impls.report

import com.example.data.models.ReportDTO
import com.example.domain.repositories.report.submitReport
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class submitReportImpl(private val db : MongoDatabase) : submitReport {
    private val reports = db.getCollection<ReportDTO>("reports")
    override suspend fun submitReport(reportDTO: ReportDTO) {
        try {
            reports.insertOne(reportDTO)
        }catch (e : Exception){
            throw e
        }
    }
}