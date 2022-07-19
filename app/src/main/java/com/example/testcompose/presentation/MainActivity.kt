package com.example.testcompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testcompose.presentation.currency_list.CurrencyListScreen
import com.example.testcompose.presentation.favourites_currency_list.FavouritesCurrencyListScreen
import com.example.testcompose.presentation.util.Screen
import com.example.testcompose.ui.theme.TestComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.CurrencyListScreen.route
                        ) {
                            composable(route = Screen.CurrencyListScreen.route) {
                                CurrencyListScreen(navController = navController)
                            }
                            composable(route = Screen.FavouritesCurrencyListScreen.route) {
                                FavouritesCurrencyListScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavController) {
    val screens = listOf(
        Screen.CurrencyListScreen,
        Screen.FavouritesCurrencyListScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 5.dp
    ) {
        screens.forEach { screen ->
            val selected = screen.route == navBackStackEntry?.destination?.route
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Default.Alarm, contentDescription = "")
                })
        }
    }
}

