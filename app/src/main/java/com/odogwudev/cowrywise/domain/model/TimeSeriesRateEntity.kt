package com.odogwudev.cowrywise.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timeseries_rates")
data class TimeSeriesRateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val base: String,
    val currencyCode: String,
    val rate: Double
)