package com.example.testcompose.presentation.util

sealed class Screen(val route: String) {
    object CurrencyListScreen: Screen("currency_list_screen")
    object FavouritesCurrencyListScreen: Screen("favourites_currency_list_screen")
}