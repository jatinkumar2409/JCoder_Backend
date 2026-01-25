package com.example.presentation.routes.report

import com.example.data.models.ReportDTO
import com.example.domain.usecases.report.submitReportUseCase
import com.example.domain.usecases.verifyToken.verifyTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.reportRoute(){
    post("/submitReport"){
       val token = call.queryParameters["token"] ?: run {
           call.respond(status = HttpStatusCode.BadRequest , message = "Invalid token")
           return@post
       }
       val report = call.receive<ReportDTO>()
       val submitReport by inject<submitReportUseCase>()
        val verifyToken by inject<verifyTokenUseCase>()
       try {
           val user = verifyToken.verifyToken(token)
           submitReport.submitReport(report)
           call.respond(status = HttpStatusCode.OK , message = "Report Saved")
       }catch (e : Exception){
           call.respond(status = HttpStatusCode.ExpectationFailed , message = "Something went wrong")
       }
    }
}