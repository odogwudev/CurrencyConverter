package com.odogwudev.cowrywise.presenntation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity
import com.odogwudev.cowrywise.data.repository.FixerRepository
import com.odogwudev.cowrywise.data.repository.Resource
import com.odogwudev.cowrywise.domain.model.SymbolEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FixerViewModel @Inject constructor(
    private val repository: FixerRepository
) : ViewModel() {

    private val _ratesState = MutableStateFlow<Resource<List<ExchangeRateEntity>>>(Resource.Loading)
    val ratesState = _ratesState.asStateFlow()

    fun loadLatestRates(base: String,symbols: String) {
        viewModelScope.launch {
            repository.getLatestRates(base,symbols).collectLatest { resource ->
                _ratesState.value = resource
            }
        }
    }

    private val _symbolsState = MutableStateFlow<Resource<List<SymbolEntity>>>(Resource.Loading)
    val symbolsState = _symbolsState.asStateFlow()

    fun loadSymbols() {
        viewModelScope.launch {
            repository.getSymbols().collect { resource ->
                _symbolsState.value = resource
            }
        }
    }

    private val _latestRatesState = MutableStateFlow<Resource<List<ExchangeRateEntity>>>(Resource.Loading)
    val latestRatesState = _latestRatesState.asStateFlow()

    fun loadLatestRates(base: String?, symbols: String?) {
        viewModelScope.launch {
            repository.getLatestRates(base, symbols).collect {
                _latestRatesState.value = it
            }
        }
    }
}