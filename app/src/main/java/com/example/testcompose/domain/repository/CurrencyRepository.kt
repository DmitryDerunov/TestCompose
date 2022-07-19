package com.example.testcompose.domain.repository

import com.example.testcompose.common.Resource
import com.example.testcompose.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun getCurrencyList(): Flow<Resource<List<Currency>>>
}