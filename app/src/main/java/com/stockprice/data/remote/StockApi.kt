package com.stockprice.data.remote

import com.stockprice.utils.API_KEY
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getStocks(@Query("apikey") apiKey: String = API_KEY): ResponseBody
}
