package com.stockprice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyListing")
data class CompanyEntity(
    @PrimaryKey
    val id: Int? = null,
    val name: String,
    val symbol: String,
    val exchange: String
)
