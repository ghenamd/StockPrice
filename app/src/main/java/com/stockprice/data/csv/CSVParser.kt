package com.stockprice.data.csv

import com.stockprice.data.remote.IntraDayInfoDto
import com.stockprice.domain.model.Company
import java.io.InputStream

interface CSVParser {
    suspend fun parseToCompany(stream: InputStream): List<Company>
    suspend fun parseToIntraDayInfo(stream: InputStream): List<IntraDayInfoDto>
}
