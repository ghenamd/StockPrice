package com.stockprice.presentation.companylist.ui

import com.stockprice.domain.model.Company

data class CompanyListState(
    val companies: List<Company> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
