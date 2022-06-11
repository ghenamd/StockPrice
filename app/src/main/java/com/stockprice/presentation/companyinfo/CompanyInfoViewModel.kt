package com.stockprice.presentation.companyinfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockprice.domain.repository.StockRepository
import com.stockprice.presentation.companyinfo.ui.CompanyInfoState
import com.stockprice.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val repository: StockRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            val companyInfo = async { repository.getCompanyInfo(symbol) }
            val intraDays = async { repository.getIntraDayInfo(symbol) }

            when (val result = companyInfo.await()) {
                is Response.Success -> {
                    state = state.copy(companyInfo = result.data, isLoading = false)
                }
                is Response.Error -> {
                    state = state.copy(error = result.error, isLoading = false)
                }
                else -> Unit
            }

            when (val result = intraDays.await()) {
                is Response.Success -> {
                    state = state.copy(stockInfo = result.data ?: emptyList(), isLoading = false)
                }
                is Response.Error -> {
                    state = state.copy(error = result.error, isLoading = false)
                }
                else -> Unit
            }
        }
    }
}
