package com.example.testcompose.domain.use_case

import com.example.testcompose.common.Resource
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetCurrencyListUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Currency>>> {
        return repository.getCurrencyList()
    }
}