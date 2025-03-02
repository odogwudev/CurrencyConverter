package com.odogwudev.cowrywise.domain.model

import com.odogwudev.cowrywise.data.model.ExchangeRatesResponse
import com.odogwudev.cowrywise.data.model.SymbolsResponse
import com.odogwudev.cowrywise.data.model.convert.ConvertResponse
import com.odogwudev.cowrywise.data.model.fluctuationresponse.FluctuationResponse
import com.odogwudev.cowrywise.data.model.timeseries.TimeSeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerApi {

    /**
     * 1) Supported Symbols Endpoint
     *    GET /symbols
     */
    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") apiKey: String
    ): SymbolsResponse

    /**
     * 2) Latest Rates Endpoint
     *    GET /latest
     */
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null
    ): ExchangeRatesResponse

    /**
     * 3) Historical Rates Endpoint
     *    GET /{date}
     *    date format: YYYY-MM-DD
     */
    @GET("{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @Query("access_key") apiKey: String,
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null
    ): ExchangeRatesResponse

    /**
     * 4) Convert Endpoint
     *    GET /convert
     */
    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") apiKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
        @Query("date") date: String? = null  // optional for historical
    ): ConvertResponse

    /**
     * 5) Time-Series Endpoint
     *    GET /timeseries
     */
    @GET("timeseries")
    suspend fun getTimeSeries(
        @Query("access_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null
    ): TimeSeriesResponse

    /**
     * 6) Fluctuation Endpoint
     *    GET /fluctuation
     */
    @GET("fluctuation")
    suspend fun getFluctuation(
        @Query("access_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null
    ): FluctuationResponse
}
