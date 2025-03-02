package com.odogwudev.cowrywise.data.model.timeseries

data class TimeSeriesResponse(
    val success: Boolean,
    val timeseries: Boolean?,
    val start_date: String?,
    val end_date: String?,
    val base: String?,
    val rates: Map<String, Map<String, Double>>? 
    // e.g. {
    //   "2012-05-01": {"USD":1.322891, "AUD":1.278047, "CAD":1.302303}, 
    //   "2012-05-02": ...
    // }
)