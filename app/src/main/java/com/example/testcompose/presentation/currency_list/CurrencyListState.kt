package com.example.testcompose.presentation.currency_list

import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.util.CurrencyOrder
import com.example.testcompose.domain.util.OrderType

data class CurrencyListState(
    val isLoading: Boolean = false,
    val currencyList: List<Currency> = emptyList(),
    val isOrderSectionVisible: Boolean = false,
    val currencyOrder: CurrencyOrder = CurrencyOrder.Description(OrderType.Descending)
)