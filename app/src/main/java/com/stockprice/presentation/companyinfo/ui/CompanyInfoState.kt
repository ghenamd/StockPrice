package com.stockprice.presentation.companyinfo.ui

import com.stockprice.domain.model.CompanyInfo
import com.stockprice.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo: List<IntraDayInfo> = emptyList(),
    val companyInfo: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
