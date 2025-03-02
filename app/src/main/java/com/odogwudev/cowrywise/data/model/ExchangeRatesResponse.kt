package com.odogwudev.cowrywise.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>,
    @SerializedName("historical")
    val historical: Boolean?
)