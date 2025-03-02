package com.odogwudev.cowrywise.data.model

data class SymbolsResponse(
    val success: Boolean,
    val symbols: Map<String, String>?
)