package com.example.testcompose.presentation.favourites_currency_list

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.presentation.currency_list.CurrencyListViewModel
import com.example.testcompose.presentation.currency_list.components.CurrencyListItem
import com.example.testcompose.presentation.currency_list.components.OrderSection

@ExperimentalFoundationApi
@Composable
fun FavouritesCurrencyListScreen(
    navController: NavController,
    viewModel: FavouritesCurrencyListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Your note", style = MaterialTheme.typography.h4)
                    IconButton(
                        onClick = {
                            viewModel.toggleOrderSection()
                        }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Setting")
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        currencyOrder = state.currencyOrder,
                        onOrderChange = {
                            viewModel.order(it)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.currencyList) { currency ->
                        CurrencyListItem(
                            currency = currency,
                            modifier = Modifier.fillMaxWidth(),
                            onFavouriteCLick = {

                            })
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}