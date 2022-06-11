package com.stockprice.data.repository

import com.stockprice.data.csv.CSVParser
import com.stockprice.data.local.StockDatabase
import com.stockprice.data.mapper.CompanyInfoMapper
import com.stockprice.data.mapper.CompanyMapper
import com.stockprice.data.remote.StockApi
import com.stockprice.domain.model.Company
import com.stockprice.domain.model.CompanyInfo
import com.stockprice.domain.model.IntraDayInfo
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
    private val companyMapper: CompanyMapper,
    private val companyInfoMapper: CompanyInfoMapper,
    private val parser: CSVParser
) : StockRepository {

    override suspend fun getCompanies(
        fetchRemotely: Boolean,
        query: String
    ): Flow<Response<List<Company>>> {
        return flow {
            emit(Response.Loading(isLoading = true))
            val companies = db.dao.searchCompanies(query = query)
            emit(Response.Loading(isLoading = false))
            emit(Response.Success(data = companyMapper.toCompanies(companies)))
            if (companies.isNotEmpty() && !fetchRemotely) return@flow

            val remoteCompanies = try {
                val response = api.getStocks()
                val stream = response.byteStream()
                parser.parseToCompany(stream)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error("Failed to fetch Company"))
                null
            }

            remoteCompanies?.let {
                emit(Response.Loading(isLoading = false))
                emit(Response.Success(data = it))
                db.dao.clearDb()
                db.dao.insertCompanies(companyMapper.toEntity(it))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Response<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val parsed = parser.parseToIntraDayInfo(response.byteStream())
            Response.Success(data = companyInfoMapper.toIntraDayInfo(parsed))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Error("Failed to fetch IntraDayInfo")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Response<List<CompanyInfo>> {
        return try {
            val response = api.getCompanyInfo(symbol)
            Response.Success(data = companyInfoMapper.toCompanyInfo(response))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Error("Failed to fetch CompanyInfo")
        }
    }
}
