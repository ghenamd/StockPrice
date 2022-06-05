package com.stockprice.domain.model

data class CompanyState(
    val companies: List<Company> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
