package com.stockprice.data.repository

import com.stockprice.data.csv.CSVParser
import com.stockprice.data.local.StockDatabase
import com.stockprice.data.mapper.CompanyMapper
import com.stockprice.data.remote.StockApi
import com.stockprice.domain.model.Company
import com.stockprice.domain.repository.StockRepository
import com.stockprice.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val mapper: CompanyMapper,
    private val parser: CSVParser<Company>
) : StockRepository {

    override suspend fun getCompanies(
        fetchRemotely: Boolean,
        query: String
    ): Flow<Response<List<Company>>> {
        return flow {
            emit(Response.Loading(isLoading = true))
            val companies = db.dao.searchCompanies(query = query)
            emit(Response.Loading(isLoading = false))
            emit(Response.Success(data = mapper.toCompanies(companies)))
            if (companies.isNotEmpty() && !fetchRemotely) return@flow

            val remoteCompanies = try {
                val response = api.getStocks()
                val stream = response.byteStream()
                parser.parse(stream)
            } catch (e: Exception) {
                emit(Response.Error("Failed to fetch data"))
                null
            }

            remoteCompanies?.let {
                emit(Response.Loading(isLoading = false))
                emit(Response.Success(data = it))
                db.dao.clearDb()
                db.dao.insertCompanies(mapper.toEntity(it))
            }
        }
    }
}
