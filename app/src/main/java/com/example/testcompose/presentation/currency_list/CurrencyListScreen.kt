package com.example.testcompose.presentation.currency_list

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.domain.model.Currency
import com.example.testcompose.presentation.currency_list.components.CurrencyListItem
import com.example.testcompose.presentation.currency_list.components.OrderSection

@ExperimentalFoundationApi
@Composable
fun CurrencyListScreen(
    navController: NavController,
    viewModel: CurrencyListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit ){
        viewModel.showMessage.collect{
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.border(1.dp, MaterialTheme.colors.primary),
                            onClick = {
                                viewModel.dropDownVisibility(true)
                        }) {
                            Text(modifier = Modifier
                                .padding(16.dp),
                                text = state.baseCurrency, style = MaterialTheme.typography.h6)
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Relative currency")
                        }
                        DropdownMenu(modifier = Modifier
                            .width(150.dp)
                            .height((LocalConfiguration.current.screenHeightDp / 2).dp), expanded = state.isDropDownShown, onDismissRequest = { viewModel.dropDownVisibility(false)}) {
                            state.currencyList.forEach { currency ->
                                DropdownMenuItem(onClick = { /*TODO*/ }) {
                                    ClickableText(text = AnnotatedString(currency.description), onClick = {
                                        viewModel.changeRelativeCurrency(currency)
                                    })
                                }
                            }
                        }
                        IconButton(
                            onClick = {
                                viewModel.toggleOrderSection()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Setting"
                            )
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
                                    viewModel.addToFavourite(currency)
                                })
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colors.primary, RectangleShape)
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically)) {
                        IconButton(modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                            onClick = {
                                viewModel.showOnlyFavourites(false)
                            }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (state.showOnlyFavourites) Color.Gray else Color.White,
                            )
                        }
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically)) {
                        IconButton(modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                            onClick = {
                                viewModel.showOnlyFavourites(true)
                            }) {
                            Icon(
                                imageVector = Icons.Default.StarRate,
                                contentDescription = "Favourites",
                                tint = if (state.showOnlyFavourites) Color.White else Color.Gray
                            )
                        }

                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
