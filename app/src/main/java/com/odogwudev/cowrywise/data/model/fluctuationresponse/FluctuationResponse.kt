package com.odogwudev.cowrywise.data.model.fluctuationresponse

data class FluctuationResponse(
    val success: Boolean,
    val fluctuation: Boolean?,
    val start_date: String?,
    val end_date: String?,
    val base: String?,
    val rates: Map<String, FluctuationData>?
)

data class FluctuationData(
    val start_rate: Double?,
    val end_rate: Double?,
    val change: Double?,
    val change_pct: Double?
)
