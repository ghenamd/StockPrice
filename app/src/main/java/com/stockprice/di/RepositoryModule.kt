package com.stockprice.di

import com.stockprice.data.csv.CSVParser
import com.stockprice.data.csv.CSVParserImpl
import com.stockprice.data.repository.StockRepositoryImpl
import com.stockprice.domain.model.Company
import com.stockprice.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsStockRepository(impl: StockRepositoryImpl): StockRepository

    @Binds
    abstract fun bindsCsvParser(impl: CSVParserImpl): CSVParser<Company>
}
