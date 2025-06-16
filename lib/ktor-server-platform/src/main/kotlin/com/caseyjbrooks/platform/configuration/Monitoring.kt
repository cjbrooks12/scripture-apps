package com.caseyjbrooks.platform.configuration

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.core.KoinApplication
import org.slf4j.event.Level
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun Application.configureMonitoring(koinApplication: KoinApplication) {
    install(MicrometerMetrics) {
        registry = koinApplication.koin.get<PrometheusMeterRegistry>()
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .maximumExpectedValue(20.seconds.inWholeNanoseconds.toDouble())
            .serviceLevelObjectives(
                100.milliseconds.inWholeNanoseconds.toDouble(),
                500.milliseconds.inWholeNanoseconds.toDouble()
            )
            .build()
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
        )
    }
    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            if(call.request.path() == "/api/v1/public/health") return@filter false
            if(call.request.path() == "/api/v1/public/metrics") return@filter false
            call.request.path().startsWith("/")
        }

        callIdMdc("call-id")
    }
    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
}
