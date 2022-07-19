package com.example.testcompose.presentation.currency_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testcompose.domain.util.CurrencyOrder
import com.example.testcompose.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    currencyOrder: CurrencyOrder = CurrencyOrder.Description(OrderType.Descending),
    onOrderChange: (CurrencyOrder) -> Unit
){
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Description",
                selected = currencyOrder is CurrencyOrder.Description,
                onSelect = { onOrderChange(CurrencyOrder.Description(currencyOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Value",
                selected = currencyOrder is CurrencyOrder.Value,
                onSelect = { onOrderChange(CurrencyOrder.Value(currencyOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Ascending",
                selected = currencyOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(currencyOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = currencyOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(currencyOrder.copy(OrderType.Descending)) }
            )
        }
    }
}