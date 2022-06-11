package com.stockprice.data.csv

import com.opencsv.CSVReader
import com.stockprice.data.remote.IntraDayInfoDto
import com.stockprice.domain.model.Company
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CSVParserImpl @Inject constructor() : CSVParser {

    override suspend fun parseToCompany(stream: InputStream): List<Company> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    Company(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null,
                    )
                }.also {
                    csvReader.close()
                }
        }
    }

    override suspend fun parseToIntraDayInfo(stream: InputStream): List<IntraDayInfoDto> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0)
                    val close = line.getOrNull(4)
                    IntraDayInfoDto(
                        timestamp = timestamp ?: return@mapNotNull null,
                        close = close?.toDouble() ?: return@mapNotNull null
                    )
                }.also {
                    csvReader.close()
                }
        }
    }
}
