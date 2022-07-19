package com.example.testcompose.domain.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}