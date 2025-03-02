package com.odogwudev.cowrywise.data.model.convert

data class ConvertResponse(
    val success: Boolean,
    val query: ConvertQuery?,
    val info: ConvertInfo?,
    val historical: Boolean?,
    val date: String?,
    val result: Double?
)

data class ConvertQuery(
    val from: String?,
    val to: String?,
    val amount: Double?
)

data class ConvertInfo(
    val timestamp: Long?,
    val rate: Double?
)