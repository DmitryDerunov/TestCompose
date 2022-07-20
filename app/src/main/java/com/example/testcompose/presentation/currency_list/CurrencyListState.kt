package com.example.testcompose.presentation.currency_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.util.CurrencyOrder
import com.example.testcompose.domain.util.OrderType

data class CurrencyListScreenState(
    val isLoading: Boolean = false,
    val currencyList: List<Currency> = emptyList(),
    val isOrderSectionVisible: Boolean = false,
    val currencyOrder: CurrencyOrder = CurrencyOrder.Description(OrderType.Descending),
    val showOnlyFavourites: Boolean = false
)