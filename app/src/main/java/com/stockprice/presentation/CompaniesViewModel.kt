package com.stockprice.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockprice.domain.model.CompanyState
import com.stockprice.domain.repository.StockRepository
import com.stockprice.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyState())
    private var searchJob: Job? = null

    init {
        getCompanies()
    }

    fun onEvent(event: CompanyEvent) {
        when (event) {
            is CompanyEvent.Refresh -> {
                getCompanies(fetchRemotely = true)
            }
            is CompanyEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    getCompanies()
                }
            }
        }
    }


    private fun getCompanies(
        query: String = state.searchQuery.lowercase(),
        fetchRemotely: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanies(fetchRemotely, query)
                .collect { result ->
                    when (result) {
                        is Response.Success -> {
                            result.data?.let {
                                state = state.copy(companies = result.data)
                            }
                        }
                        is Response.Error -> {

                        }
                        is Response.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}