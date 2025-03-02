package com.odogwudev.cowrywise.data.repository

import android.util.Log
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity
import com.odogwudev.cowrywise.data.model.fluctuationresponse.FluctuationResponse
import com.odogwudev.cowrywise.domain.model.ExchangeRateDao
import com.odogwudev.cowrywise.domain.model.FixerApi
import com.odogwudev.cowrywise.domain.model.SymbolEntity
import com.odogwudev.cowrywise.domain.model.SymbolsDao
import com.odogwudev.cowrywise.domain.model.TimeSeriesDao
import com.odogwudev.cowrywise.domain.model.TimeSeriesRateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FixerRepository @Inject constructor(
    private val api: FixerApi,
    private val symbolsDao: SymbolsDao,
    private val exchangeDao: ExchangeRateDao,
    private val timeSeriesDao: TimeSeriesDao
) {
    private val apiKey = "bb7946da9b6b0a7f87ac0987ab0c58d0"


    // 1) Get All Symbols
    fun getSymbols(): Flow<Resource<List<SymbolEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val remote = api.getSymbols(apiKey)
            if (remote.success && remote.symbols != null) {
                // Clear local, then insert
                symbolsDao.clearAllSymbols()
                val symbolsList = remote.symbols.map { (code, name) ->
                    SymbolEntity(code, name)
                }
                symbolsDao.insertSymbols(symbolsList)
            } else {
                emit(Resource.Error("Failed to fetch symbols"))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network Error: ${e.localizedMessage}"))
            return@flow
        }

        // now read from DB
        val dbFlow = symbolsDao.getAllSymbols().map { list ->
            Resource.Success(list) as Resource<List<SymbolEntity>>
        }
        emitAll(dbFlow)
    }



    /**
     * Listen for exchange rates from local DB as a Flow.
     * Optionally, refresh from network if needed.
     */
    fun getLatestRates(
        base: String?,
        symbols: String?
    ): Flow<Resource<List<ExchangeRateEntity>>> = flow {
        try {
            // Emit Loading
            emit(Resource.Loading)

            // 1) Call API
            val remoteResponse = api.getLatestRates(
                apiKey = apiKey,
                base = base,               // e.g. "USD"
                symbols = symbols  // limit to certain currencies if needed
            )

            // 2) If success, parse and insert into local DB
            if (remoteResponse.success && remoteResponse.rates != null && remoteResponse.date != null && remoteResponse.base != null) {
                // Clear existing base
                exchangeDao.deleteRatesByBase(remoteResponse.base)

                val date = remoteResponse.date
                val rateEntities = remoteResponse.rates.map { (currencyCode, rateValue) ->
                    ExchangeRateEntity(
                        currencyCode = currencyCode,
                        rate = rateValue,
                        base = remoteResponse.base,
                        date = date
                    )
                }
                exchangeDao.insertRates(rateEntities)
            } else {
                // If the response indicates an error, handle accordingly
                emit(Resource.Error("API call failed"))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
            return@flow
        }
        val dbFlow = exchangeDao.getRatesByBase(base?:"EUR").map { rates ->
            Resource.Success(rates) as Resource<List<ExchangeRateEntity>>
        }
        emitAll(dbFlow)

    }

    suspend fun convertLocally(
        from: String,
        to: String,
        amount: Double
    ): Flow<Resource<Double>> = flow {
        emit(Resource.Loading)
        try {
            // 1) Attempt to get rates from DB (where base="EUR").
            var fromEntity = exchangeDao.getRateByCurrency("EUR", from)
            var toEntity   = exchangeDao.getRateByCurrency("EUR", to)

            // 2) If missing, do a one-time fetch.
            if (fromEntity == null || toEntity == null) {
                val neededSymbols = listOf(from, to, "EUR")
                    .filter { it.isNotBlank() }
                    .joinToString(",")
                // *** Single function call, not a flow. ***
                val fetchResult = fetchLatestRatesOnce(base = "", symbols = neededSymbols)
                if (fetchResult is Resource.Error) {
                    // Return the error
                    emit(Resource.Error(fetchResult.message, fetchResult.cause))
                    return@flow
                }

                // re-check the DB after success
                fromEntity = exchangeDao.getRateByCurrency("EUR", from)
                toEntity   = exchangeDao.getRateByCurrency("EUR", to)
            }

            // 4) If STILL missing, we cannot convert.
            if (fromEntity == null || toEntity == null) {
                emit(Resource.Error("Missing rate for $from or $to"))
                return@flow
            }

            val fromRate = fromEntity.rate
            val toRate   = toEntity.rate
            val result   = (amount / fromRate) * toRate
            emit(Resource.Success(result.round()))

        } catch (e: Exception) {
            emit(Resource.Error("Conversion failed: ${e.localizedMessage}", e))
        }
    }
    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    // Instead of returning Flow<Resource<List<ExchangeRateEntity>>>
    suspend fun fetchLatestRatesOnce(
        base: String,
        symbols: String
    ): Resource<List<ExchangeRateEntity>> {
        return try {
            val remoteResponse = api.getLatestRates(
                apiKey = apiKey,
                base = base,
                symbols = symbols
            )

            if (remoteResponse.success && remoteResponse.rates != null
                && remoteResponse.date != null && remoteResponse.base != null) {

                // Store in DB
                exchangeDao.deleteRatesByBase(remoteResponse.base)
                val date = remoteResponse.date
                val rateEntities = remoteResponse.rates.map { (currencyCode, rateValue) ->
                    ExchangeRateEntity(
                        currencyCode = currencyCode,
                        rate = rateValue,
                        base = remoteResponse.base,
                        date = date
                    )
                }
                exchangeDao.insertRates(rateEntities)

                // Now read from DB again
                val updatedList = exchangeDao.getRatesByBaseOnce(remoteResponse.base)
                Resource.Success(updatedList)
            } else {
                Resource.Error("API call failed or invalid response")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}", e)
        }
    }



    // 3) Historical Rates
    fun getHistoricalRates(
        date: String,
        base: String?,
        symbols: String?
    ): Flow<Resource<List<ExchangeRateEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getHistoricalRates(
                date = date,
                apiKey = apiKey,
                base = base,
                symbols = symbols
            )
            if (response.success && response.rates != null && response.base != null) {
                // Optionally store them in DB. E.g. prefix them with date in your table
                val entities = response.rates.map { (currency, rate) ->
                    ExchangeRateEntity(
                        currencyCode = currency,
                        rate = rate,
                        base = response.base,
                        date = date
                    )
                }
                exchangeDao.insertRates(entities)
            } else {
                emit(Resource.Error("Historical rates fetch failed"))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
            return@flow
        }

        // Optionally read from DB filtered by date & base if you store them that way
        val dbFlow = exchangeDao.getRatesByBase(base ?: "EUR").map { list ->
            // Filter by date if needed or store differently
            val onlyThisDate = list.filter { it.date == date }
            Resource.Success(onlyThisDate) as Resource<List<ExchangeRateEntity>>
        }
        emitAll(dbFlow)
    }

    // 4) Convert Endpoint
    fun convert(
        from: String,
        to: String,
        amount: Double,
        date: String? = null
    ): Flow<Resource<Double>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.convertCurrency(
                apiKey = apiKey,
                from = from,
                to = to,
                amount = amount,
                date = date
            )
            if (response.success && response.result != null) {
                emit(Resource.Success(response.result))
            } else {
                emit(Resource.Error("Conversion failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }

    // 5) Time-Series Endpoint
    fun getTimeSeries(
        startDate: String,
        endDate: String,
        base: String?,
        symbols: String?
    ): Flow<Resource<List<TimeSeriesRateEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTimeSeries(
                apiKey = apiKey,
                startDate = startDate,
                endDate = endDate,
                base = base,
                symbols = symbols
            )
            if (response.success && response.timeseries == true && response.rates != null) {
                val list = mutableListOf<TimeSeriesRateEntity>()
                val baseCurrency = response.base ?: "EUR"
                for ((day, currencyMap) in response.rates) {
                    for ((currency, rate) in currencyMap) {
                        list.add(
                            TimeSeriesRateEntity(
                                date = day,
                                base = baseCurrency,
                                currencyCode = currency,
                                rate = rate
                            )
                        )
                    }
                }
                // Possibly clear old data in your timeSeriesDao for the range
                timeSeriesDao.insertTimeSeriesRates(list)
                emit(Resource.Success(list))
            } else {
                emit(Resource.Error("TimeSeries fetch failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }

    // 6) Fluctuation Endpoint
    fun getFluctuation(
        startDate: String,
        endDate: String,
        base: String?,
        symbols: String?
    ): Flow<Resource<FluctuationResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getFluctuation(
                apiKey = apiKey,
                startDate = startDate,
                endDate = endDate,
                base = base,
                symbols = symbols
            )
            if (response.success && response.fluctuation == true) {
                // If you want, parse and store in DB. For example:
                // [Not showing a DAO for fluctuation but the pattern is the same]
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error("Fluctuation fetch failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}