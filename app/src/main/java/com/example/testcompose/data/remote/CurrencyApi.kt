package com.example.testcompose.data.remote

import com.example.testcompose.data.remote.dto.CurrencyResponse
import retrofit2.http.GET

interface CurrencyApi {

    @GET("/exchangerates_data/latest")
    suspend fun getCurrencyList(): CurrencyResponse
}