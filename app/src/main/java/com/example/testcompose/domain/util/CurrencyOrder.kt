package com.example.testcompose.domain.util

sealed class CurrencyOrder(val orderType: OrderType){
    class Description(orderType: OrderType): CurrencyOrder(orderType)
    class Value(orderType: OrderType): CurrencyOrder(orderType)

    fun copy(orderType: OrderType): CurrencyOrder{
        return when(this){
            is Description -> Description(orderType)
            is Value -> Value(orderType)
        }
    }
}