package com.example.testcompose.data.local

import androidx.room.*
import com.example.testcompose.domain.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    suspend fun getCurrencyList(): List<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currencyList: List<Currency>)

    @Update
    suspend fun updateCurrency(currency: Currency)

    @Delete
    suspend fun deleteCurrency(currency: Currency)
}