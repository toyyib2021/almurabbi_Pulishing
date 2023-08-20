package com.stevdza.san.mongodemo.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stevdza.san.mongodemo.screen.pin_screen.PinCreationScreen
import com.stevdza.san.mongodemo.screen.pin_screen.PinScreen
import com.stevdza.san.mongodemo.screen.splah.Splash
import com.stevdza.san.mongodemo.ui.Constants.AUTH_GRAPH_ROUTE
import com.stevdza.san.mongodemo.ui.Constants.DASHBOARD_GRAPH_ROUTE


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authRoute(
    navController: NavHostController
) {

    navigation(
        startDestination = Screen.Splash.route,
        route = AUTH_GRAPH_ROUTE
    ){
        composable(route = Screen.Splash.route) {
            Splash(navController)
        }

        composable(route = Screen.PinCreation.route){
            PinCreationScreen(
                navToDashBoard = { navController.navigate((DASHBOARD_GRAPH_ROUTE)) }
            )
        }

        composable(route = Screen.PinLogin.route){
            PinScreen(
                navPinCreationScreen = { navController.navigate(Screen.PinCreation.route) },
                navDashboardScreen = {navController.navigate(DASHBOARD_GRAPH_ROUTE)}
            )
        }

    }
}