package com.stockprice.presentation.companylist

sealed class CompanyEvent {
    object Refresh : CompanyEvent()
    data class OnSearchQueryChange(val query: String) : CompanyEvent()
}
