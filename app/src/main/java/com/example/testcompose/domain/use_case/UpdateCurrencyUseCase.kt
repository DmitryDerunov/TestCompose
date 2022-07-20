package com.example.testcompose.domain.use_case

import com.example.testcompose.data.local.CurrencyDao
import com.example.testcompose.domain.model.Currency

class UpdateCurrencyUseCase(
    private val dao: CurrencyDao
) {
    suspend operator fun invoke(currency: Currency){
        dao.updateCurrency(currency)
    }
}