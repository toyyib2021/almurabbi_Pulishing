package com.stevdza.san.mongodemo.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.stevdza.san.mongodemo.screen.auth.AuthKeyScreen
import com.stevdza.san.mongodemo.screen.book.BookList
import com.stevdza.san.mongodemo.screen.book.EachBookRecord
import com.stevdza.san.mongodemo.screen.book.UpdateBook
import com.stevdza.san.mongodemo.screen.customer.Customer
import com.stevdza.san.mongodemo.screen.order.OrderDetail
import com.stevdza.san.mongodemo.screen.order.OrderList
import com.stevdza.san.mongodemo.screen.order.PLaceOrder
import com.stevdza.san.mongodemo.screen.homeTest.Home
import com.stevdza.san.mongodemo.screen.price.PriceList
import com.stevdza.san.mongodemo.ui.Constants.DASHBOARD_GRAPH_ROUTE

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeRoute(
    navController: NavHostController
) {

    navigation(
        startDestination = Screen.Home.route,
        route = DASHBOARD_GRAPH_ROUTE
    ) {

        composable(route = Screen.Home.route) {
            Home(
                onUpdateProductRecordClick = { navController.navigate(Screen.UpdateBook.route) },
                onOrderClick = {
                    navController.navigate(Screen.OrderDetail.passArgument2(it)) },
                onPriceListClicked = { navController.navigate(Screen.PriceList.route) },
                onCustomerListClick = { navController.navigate(Screen.CustomerList.route) },
                onViewBooksRecordClick = { navController.navigate(Screen.BookList.route) },
                onSeeAllClick = { navController.navigate(Screen.OrderRecordList.route) },
                onHiClick = {navController.navigate(Screen.Key.route)}
            )

        }

        composable(route = Screen.UpdateBook.route) {
            UpdateBook(onBackClick = {navController.navigate(Screen.Home.route)})
        }



        composable(route = Screen.PriceList.route) {
            PriceList(
                onBackClick = {navController.navigate(Screen.Home.route)}
            )

        }
        composable(route = Screen.CustomerList.route) {
            Customer(
                onBackClick = {navController.navigate(Screen.Home.route)},
                onPlaceOrderClick = {navController.navigate(Screen.PlaceOrder.passArgument(it))}
            )
        }

        composable(route = Screen.BookList.route) {
            BookList(
                onBackClick = {navController.navigate(Screen.Home.route)},
                onBookClick = { navController.navigate(Screen.EachBookRecord.passArgument(it)) }
            )
        }

        composable(
            route = Screen.EachBookRecord.route,
            arguments = listOf(
                navArgument(NAVKEY) {
                    type = NavType.StringType
                })
        ) {
            val bookName = it.arguments?.getString(NAVKEY) ?: ""

            EachBookRecord(
                onBackClick = {navController.navigate(Screen.BookList.route) },
                bookName = bookName)
        }

        composable(route = Screen.Key.route) {
            AuthKeyScreen()
        }


        // ORDER SCREEN'S
        composable(route = Screen.OrderRecordList.route) {
            OrderList(
                onOrderClick = {navController.navigate(Screen.OrderDetail.passArgument2(it))},
                onBackClick = {navController.navigate(Screen.Home.route) }
            )
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(
                navArgument(NAVKEY) {
                    type = NavType.StringType
                })
        ) {
            val orderId = it.arguments?.getString(NAVKEY) ?: ""

            OrderDetail(id = orderId,
                onBackClick = {navController.navigate(Screen.Home.route)},
                navToHomeScreen = {navController.navigate(Screen.Home.route)}
            )
        }

        composable(
            route = Screen.PlaceOrder.route,
            arguments = listOf(
                navArgument(NAVKEY) {
                    type = NavType.StringType
                })
        ) {
            val orderId = it.arguments?.getString(NAVKEY) ?: ""
            PLaceOrder(
                id = orderId,
                onBackClick = {navController.navigate(Screen.Home.route)},
                navToHomeScreen = { navController.navigate(Screen.Home.route) }
            )
        }

    }


}

//            val viewModel: HomeViewModel = viewModel()
//            val data by viewModel.data

//            HomeScreen(
//                data = data,
//                filtered = viewModel.filtered.value,
//                name = viewModel.name.value,
//                objectId = viewModel.objectId.value,
//                onNameChanged = viewModel::updateName,
//                onObjectIdChanged = viewModel::updateObjectId,
//                onInsertClicked = viewModel::insertPerson,
//                onUpdateClicked = viewModel::updatePerson,
//                onDeleteClicked = viewModel::deletePerson,
//                onFilterClicked = viewModel::filterData,
//                streetNumber = viewModel.streetNumber.value,
//                streetName = viewModel.streetName.value,
//                onStreetNumberChanged = viewModel::updateStreetNumber,
//                onStreetNameChanged = viewModel::updateStreetName,
//                onPetChanged = viewModel::updatePetName,
//                petName = viewModel.petName.value,
//                onAddPetClicked = viewModel::addPets
//            )
//        }