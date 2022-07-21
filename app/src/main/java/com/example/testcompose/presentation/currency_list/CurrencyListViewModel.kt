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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {

    private var allCurrencies: List<Currency> = emptyList()

    private val _state = mutableStateOf(CurrencyListScreenState())
    val state: State<CurrencyListScreenState> = _state

    private val _showMessage = Channel<String>()
    val showMessage = _showMessage.receiveAsFlow()

    init {
        getCurrencyList()
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            getCurrencyListUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            initState(it)
                        }
                    }
                    is Resource.Error -> {
                        result.data?.let {
                            initState(it)
                        }
                        result.message?.let {
                            _showMessage.send(it)
                        }
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
        _state.value = _state.value.copy(
            showOnlyFavourites = showOnlyFavourites,
            currencyList = if(showOnlyFavourites) allCurrencies.filter { it.isFavourite } else allCurrencies
        )
    }

    fun addToFavourite(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyUseCase.invoke(currency)
            allCurrencies = allCurrencies.map { if (it == currency) currency.copy(isFavourite = !currency.isFavourite) else it  }
            _state.value = _state.value.copy(currencyList =
            if(_state.value.showOnlyFavourites) allCurrencies.filter { it.isFavourite } else allCurrencies)
        }
    }

    fun toggleOrderSection() {
        _state.value =
            _state.value.copy(isOrderSectionVisible = !_state.value.isOrderSectionVisible)
    }

    fun changeRelativeCurrency(currency: Currency){

    }


    private fun initState(currencies: List<Currency>) {
        _state.value = CurrencyListScreenState(
            currencyList = sortCurrenciesByOrder(
                currencies,
                _state.value.currencyOrder
            ),
            baseCurrency = currencies.first { it.value == 1.0 }.description
        )
    }

    private fun sortCurrenciesByOrder(
        currencies: List<Currency>,
        currencyOrder: CurrencyOrder
    ): List<Currency> {
        return when (currencyOrder.orderType) {
            is OrderType.Ascending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> {
                        allCurrencies = currencies.sortedBy { it.description.lowercase() }
                        allCurrencies
                    }
                    is CurrencyOrder.Value -> {
                        allCurrencies = currencies.sortedBy { it.value }
                        allCurrencies
                    }
                }
            }
            is OrderType.Descending -> {
                when (currencyOrder) {
                    is CurrencyOrder.Description -> {
                        allCurrencies = currencies.sortedByDescending { it.description.lowercase() }
                        allCurrencies
                    }
                    is CurrencyOrder.Value -> {
                        allCurrencies = currencies.sortedByDescending { it.value }
                        allCurrencies
                    }
                }
            }
        }
    }

}