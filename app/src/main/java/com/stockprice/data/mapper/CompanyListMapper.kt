package com.stockprice.data.mapper

import com.stockprice.data.local.CompanyEntity
import com.stockprice.domain.model.Company
import javax.inject.Inject

class CompanyListMapper @Inject constructor() {

    fun toCompanies(list: List<CompanyEntity>): List<Company> {
        return list.map {
            Company(
                symbol = it.symbol,
                name = it.name,
                exchange = it.exchange
            )
        }
    }

    fun toEntity(list: List<Company>): List<CompanyEntity> {
        return list.map {
            CompanyEntity(
                symbol = it.symbol,
                name = it.name,
                exchange = it.exchange
            )
        }
    }
}
