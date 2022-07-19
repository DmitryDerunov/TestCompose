package com.example.testcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testcompose.domain.model.Currency

@Database(
    entities = [Currency::class],
    version = 1
)
abstract class CurrenciesDatabase: RoomDatabase() {

    abstract val currencyDao: CurrencyDao

    companion object {
        const val DATABASE_NAME = "currencies_db"
    }
}