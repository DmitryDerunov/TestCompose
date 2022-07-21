package com.example.testcompose.data.repository

import androidx.compose.ui.text.toLowerCase
import com.example.testcompose.common.Resource
import com.example.testcompose.data.local.CurrencyDao
import com.example.testcompose.data.remote.CurrencyApi
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.repository.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//апи возвращает слишком много данных, поэтому здесь берутся только 30 валют
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val dao: CurrencyDao
) : CurrencyRepository {
    override suspend fun getCurrencyList(): Flow<Resource<List<Currency>>> = flow {
        val take = 30
        val localCurrencyList = dao.getCurrencyList()
        val localBaseCurrency = localCurrencyList.firstOrNull { it.value == 1.0 }

        try {
            emit(Resource.Loading<List<Currency>>())
            val response = currencyApi.getCurrencyList()
            delay(1000)

            if (response.rates.isEmpty())
                emit(Resource.Success<List<Currency>>(emptyList()))

            val isBaseCurrencySame =
                if (localBaseCurrency == null) false else localBaseCurrency.description.lowercase() == response.base.lowercase()
            var currencyId = 0
            val currencyList: ArrayList<Currency> = arrayListOf()
            response.rates.asIterable().take(take).forEach {
                if(!isBaseCurrencySame) {
                    currencyList.add(Currency(it.key, it.value, id = currencyId++))
                    return@forEach
                }

                val cachedCurrency = localCurrencyList.firstOrNull { currency ->
                    currency.description.lowercase() == it.key.lowercase()
                }
                if (cachedCurrency != null) currencyList.add(
                    Currency(
                        it.key,
                        it.value,
                        cachedCurrency.isFavourite,
                        currencyId++
                    )
                ) else currencyList.add(Currency(it.key, it.value, id = currencyId++))
            }

            if (!currencyList.any { item -> item.value == 1.0 }) {
                currencyList.add(Currency(response.base, 1.0, id = currencyId++))
            }

            dao.deleteAllData()
            dao.insertCurrency(currencyList)
            emit(Resource.Success<List<Currency>>(currencyList))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<Currency>>(
                    e.message ?: "An error occured, currencies load from cache",
                    data = localCurrencyList
                )
            )
        }
    }
}