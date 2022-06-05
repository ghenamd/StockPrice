package com.stockprice.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanies(list: List<CompanyEntity>)

    @Query("DELETE FROM companyListing")
    suspend fun clearDb()

    @Query(
        """
        SELECT * FROM companyListing WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol
    """
    )
    suspend fun searchCompanies(query: String): List<CompanyEntity>
}
