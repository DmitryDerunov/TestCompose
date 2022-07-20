package com.example.testcompose.presentation.currency_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcompose.common.Resource
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.use_case.GetCurrencyListUseCase
import com.example.testcompose.domain.use_case.UpdateCurrencyUseCase
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
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CurrencyListScreenState())
    val state: State<CurrencyListScreenState> = _state

    init {
        getCurrencyList()
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            getCurrencyListUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = CurrencyListScreenState(
                                currencyList = sortCurrenciesByOrder(
                                    result.data,
                                    _state.value.currencyOrder
                                )
                            )
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _state.value = CurrencyListScreenState(isLoading = true)
                    }
                }
            }
        }
    }

    fun order(currencyOrder: CurrencyOrder) {
        if (state.value.currencyOrder::class == currencyOrder::class &&
            state.value.currencyOrder.orderType == currencyOrder.orderType
        ) {
            return
        }
        _state.value = _state.value.copy(
            currencyList = sortCurrenciesByOrder(_state.value.currencyList, currencyOrder),
            currencyOrder = currencyOrder
        )
    }

    fun showOnlyFavourites(showOnlyFavourites: Boolean) {
        _state.value = _state.value.copy(showOnlyFavourites = showOnlyFavourites)
    }

    fun addToFavourite(currency: Currency) {
        val currencyList =
            state.value.currencyList.map { if (it == currency) currency.copy(isFavourite = !currency.isFavourite) else it }
        _state.value = _state.value.copy(currencyList = currencyList)
    }

    fun toggleOrderSection() {
        _state.value =
            _state.value.copy(isOrderSectionVisible = !_state.value.isOrderSectionVisible)
    }


    private fun sortCurrenciesByOrder(
        currencies: List<Currency>,
        currencyOrder: CurrencyOrder
    ): List<Currency> {
        return when (currencyOrder.orderType) {
            is OrderType.Ascending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> currencies.sortedBy { it.description.lowercase() }
                    is CurrencyOrder.Value -> currencies.sortedBy { it.value }
                }
            }
            is OrderType.Descending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> currencies.sortedByDescending { it.description.lowercase() }
                    is CurrencyOrder.Value -> currencies.sortedByDescending { it.value }
                }
            }
        }
    }

}