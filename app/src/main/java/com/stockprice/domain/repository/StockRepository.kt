package com.stockprice.domain.repository

import com.stockprice.domain.model.Company
import com.stockprice.domain.model.CompanyInfo
import com.stockprice.domain.model.IntraDayInfo
import com.stockprice.utils.Response
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanies(
        fetchRemotely: Boolean,
        query: String
    ): Flow<Response<List<Company>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): Response<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Response<CompanyInfo>
}
