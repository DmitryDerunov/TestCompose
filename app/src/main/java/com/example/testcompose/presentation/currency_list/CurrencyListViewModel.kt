package com.example.testcompose.presentation.currency_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcompose.common.Resource
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.use_case.GetCurrencyListUseCase
import com.example.testcompose.domain.util.CurrencyOrder
import com.example.testcompose.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val getCurrencyListUseCase: GetCurrencyListUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CurrencyListState())
    val state: State<CurrencyListState> = _state

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

}