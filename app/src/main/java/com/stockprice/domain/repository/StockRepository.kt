package com.stockprice.domain.repository

import com.stockprice.domain.model.Company
import com.stockprice.utils.Response
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanies(
        fetchRemotely: Boolean,
        query: String
    ): Flow<Response<List<Company>>>
}