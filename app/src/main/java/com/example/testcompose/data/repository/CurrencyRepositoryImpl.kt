package com.example.testcompose.data.repository

import com.example.testcompose.common.Resource
import com.example.testcompose.data.local.CurrencyDao
import com.example.testcompose.data.remote.CurrencyApi
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.domain.repository.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val dao: CurrencyDao
) : CurrencyRepository {
    override suspend fun getCurrencyList(): Flow<Resource<List<Currency>>> = flow {
        try {
            emit(Resource.Loading<List<Currency>>())
            //val currency = currencyApi.getCurrency()
            val currencyList: ArrayList<Currency> = ArrayList()
            delay(1000)
            //currency.rates.forEach{ (name, value) -> currencyList.add(Currency(name, value))}
            //(1..20).map { currencyList.add(Currency(it.toString(), it.toDouble())) }
            //dao.insertCurrency(currencyList)
            //currencyList.forEach{ dao.insertCurrency(it)}
            dao.getCurrencyList().map {currencyList.add(Currency(it.description, it.value)) }
            emit(Resource.Success<List<Currency>>(currencyList))
        } catch (e: Exception) {
            emit(Resource.Error<List<Currency>>(e.message ?: "An error occured"))
        }
    }
}