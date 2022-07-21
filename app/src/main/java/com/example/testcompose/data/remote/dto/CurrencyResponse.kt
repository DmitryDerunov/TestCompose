package com.example.testcompose.data.remote.dto

data class CurrencyResponse(
    val base: String,
    val rates: Map<String, Double>
)