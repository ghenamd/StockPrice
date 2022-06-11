package com.stockprice.data.mapper

import com.stockprice.data.remote.CompanyInfoDto
import com.stockprice.data.remote.IntraDayInfoDto
import com.stockprice.domain.model.CompanyInfo
import com.stockprice.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class CompanyInfoMapper @Inject constructor() {

    fun toIntraDayInfo(list: List<IntraDayInfoDto>): List<IntraDayInfo> {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

        return list.map {
            IntraDayInfo(
                date = LocalDateTime.parse(it.timestamp, formatter),
                close = it.close
            )
        }.filter {
            it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
        }.sortedBy {
            it.date.hour
        }
    }

    fun toCompanyInfo(list: List<CompanyInfoDto>): List<CompanyInfo> {
        return list.map {
            CompanyInfo(
                symbol = it.symbol,
                description = it.description,
                name = it.name,
                country = it.country,
                industry = it.industry
            )
        }
    }
}
