package com.stevdza.san.mongodemo.navigation


const val NAVKEY = "order_iD"
sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object SignUp : Screen("sign_up_screen")

    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")

    object PinCreation : Screen("pin_creation_screen")
    object PinLogin : Screen("pin_login_screen")
//    object Dashboard : Screen("home_screen")

    object OrderRecordList : Screen("order_record_list_screen")
    object CustomerList : Screen("customer_list_screen")
    object PriceList : Screen("price_list_screen")
    object BookList : Screen("book_list_screen")

    object OrderDetail : Screen("order_detail_screen/{$NAVKEY}"){
        fun passArgument2(orderId: String): String{
            return "order_detail_screen/$orderId"
        }
    }

    object PlaceOrder : Screen("order_place_screen/{$NAVKEY}"){
        fun passArgument(order_iD: String): String{
            return "order_place_screen/$order_iD"
        }
    }

    object Key : Screen("key_screen")

    object EachBookRecord : Screen("book_detail_screen/{$NAVKEY}"){
        fun passArgument(order_iD: String): String{
            return "book_detail_screen/$order_iD"
        }
    }
    object UpdateBook : Screen("update_book_screen")



}