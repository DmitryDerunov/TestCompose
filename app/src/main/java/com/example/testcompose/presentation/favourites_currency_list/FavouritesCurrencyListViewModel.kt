package com.example.testcompose.presentation.favourites_currency_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcompose.common.Resource
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.use_case.GetCurrencyListUseCase
import com.example.testcompose.domain.util.CurrencyOrder
import com.example.testcompose.domain.util.OrderType
import com.example.testcompose.presentation.currency_list.CurrencyListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesCurrencyListViewModel @Inject constructor(
    private val getCurrencyListUseCase: GetCurrencyListUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CurrencyListState())
    val state: State<CurrencyListState> = _state

    private val qwe: Int = 3

    init {
        getCurrencyList()
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            getCurrencyListUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = CurrencyListState(currencyList = result.data ?: emptyList())
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _state.value = CurrencyListState(isLoading = true)
                    }
                }
            }
        }
    }

    public fun order(currencyOrder: CurrencyOrder) {
        if (state.value.currencyOrder::class == currencyOrder::class &&
            state.value.currencyOrder.orderType == currencyOrder.orderType
        ) {
            return
        }
        _state.value = _state.value.copy(currencyList =
        when (currencyOrder.orderType) {
            is OrderType.Ascending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> _state.value.currencyList.sortedBy { it.description.lowercase() }
                    is CurrencyOrder.Value -> _state.value.currencyList.sortedBy { it.value }
                }
            }
            is OrderType.Descending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> _state.value.currencyList.sortedByDescending { it.description.lowercase() }
                    is CurrencyOrder.Value -> _state.value.currencyList.sortedByDescending { it.value }
                }
            }
        },
            currencyOrder = currencyOrder
        )
    }

    public fun toggleOrderSection() {
        _state.value =
            _state.value.copy(isOrderSectionVisible = !_state.value.isOrderSectionVisible)
    }

    public fun asd(currency: Currency) {
        _state.value =
            _state.value.copy(isOrderSectionVisible = !_state.value.isOrderSectionVisible,
                currencyList = _state.value.currencyList.mapIndexed { index, item ->
                    if (index == _state.value.currencyList.indexOf(currency)) {
                        item.copy(isFavourite = !item.isFavourite)
                    } else {
                        item
                    }
                })
    }
}
