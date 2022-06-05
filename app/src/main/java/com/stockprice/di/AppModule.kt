package com.stockprice.di

import android.app.Application
import androidx.room.Room
import com.stockprice.data.local.StockDatabase
import com.stockprice.data.remote.StockApi
import com.stockprice.utils.Common.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideStockDb(context: Application): StockDatabase {
        return Room.databaseBuilder(context, StockDatabase::class.java, "stock.db")
            .build()
    }
}
