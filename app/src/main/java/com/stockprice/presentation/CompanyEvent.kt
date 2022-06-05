package com.stockprice.presentation

sealed class CompanyEvent {
    object Refresh : CompanyEvent()
    data class OnSearchQueryChange(val query: String) : CompanyEvent()
}
