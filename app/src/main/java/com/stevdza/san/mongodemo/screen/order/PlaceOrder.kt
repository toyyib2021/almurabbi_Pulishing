package com.stevdza.san.mongodemo.screen.order

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.data.customer.Customer
import com.stevdza.san.mongodemo.data.order.OrderDetails
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.screen.AuthVm
import com.stevdza.san.mongodemo.screen.book.BookProduceUpdate
import com.stevdza.san.mongodemo.screen.book.BookRecordVM
import com.stevdza.san.mongodemo.screen.customer.CustomerVM
import com.stevdza.san.mongodemo.screen.price.PriceVM
import com.stevdza.san.mongodemo.screen.price.TopBarPriceCustomer
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.LightBlue
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h2
import com.stevdza.san.mongodemo.screen.homeTest.OrderVm
import com.stevdza.san.mongodemo.ui.Constants.DISCOUNT_PRICE
import com.stevdza.san.mongodemo.ui.Constants.DISCOUNT_PRICE_TWO
import com.stevdza.san.mongodemo.ui.Constants.PRICE
import com.stevdza.san.mongodemo.ui.Constants.REP_PRICE
import com.stevdza.san.mongodemo.ui.Constants.REP_PRICE_DISCOUNT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PLaceOrder(
    id: String,
    onBackClick: ()-> Unit,
    navToHomeScreen: ()-> Unit,
) {
    val orderVm: OrderVm = hiltViewModel()
    val bookListVm: PriceVM = hiltViewModel()
    val customerListVm: CustomerVM = hiltViewModel()
    val bookPrices = bookListVm.priceList.value
//    val index by remember { mutableIntStateOf(0) }
//    val eachBookPrice = remember { mutableStateOf(bookPrices[index]) }


    customerListVm.schoolNameSearch.value = id
    LaunchedEffect(key1 = id){
            customerListVm.getCustomer(id)

    }

    Log.e(TAG, "id: $id", )
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, ''yy"))
    val date = orderVm.date.value
    var to by remember { mutableIntStateOf(0) }


    orderVm.date.value = formattedDate
    orderVm.orderDate.value = formattedDate

    Log.e(TAG, "date: $date",)
    val orderDetailsUI = remember { mutableStateOf<OrderDetails?>(null) }


    var state by remember { mutableStateOf("") }

//    LaunchedEffect(key1 = state) {
//        if (state == "a") {
//            orderVm.orderDetailsList
//            to = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
//
//        }
//
//    }




    var oneCustomer by remember { mutableStateOf<Customer?>(null) }
    if (customerListVm.oneCustomer.value != null){
        oneCustomer = customerListVm.oneCustomer.value
        customerListVm.oneCustomer.value?.let {
            orderVm.schoolName.value = it.schoolName
            orderVm.schoolPhone.value = it.schoolPhoneNumber
        }
    }
    Log.e(TAG, "oneCustomer: $oneCustomer", )


    val openDialog = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(true) }
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = snackbarVisible) {
        if (snackbarVisible) {
            delay(2000)
            snackbarVisible = false
        }
    }
    orderVm.totalAmount.value = to.toString()

    Log.e(TAG, "PLaceOrder: ${orderVm.totalAmount.value}",)

    // Swipe to Refresh
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            orderVm.orderDetailsList
            to = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
            delay(2000L)
            isRefreshing = false
        }
    }



    BackHandler() {  }

    val authKeyVm: AuthVm = hiltViewModel()
    val objectId by remember { mutableStateOf("64e05169576408175f7bfcca") }
    val data = authKeyVm.authById


    val accessKeyCheck = data.value?.sellerKey
    var inputAccessKey by remember { mutableStateOf("") }

    authKeyVm.objectId.value = objectId

    LaunchedEffect(key1 = true ){
        authKeyVm.getOrderListById()
    }

    if (accessKeyCheck == inputAccessKey) {
        Scaffold(
            topBar = {
                TopBarPriceCustomer(
                    onBackClick = { onBackClick() },
                    onAddClick = {
                        state = ""
                        orderVm.bookName.value = ""
                        orderVm.qty.value = ""
                        scope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    },
                    priceListTitle = "Place An Order"
                )
            },
            content = {
                val i = it
                var priceSelected by remember { mutableStateOf("") }

                Log.e(TAG, "priceSelected: $priceSelected", )
                Log.e(TAG, "priceType: ${orderVm.priceType.value}", )

                val bookProducedVm: BookRecordVM = hiltViewModel()
                val books = bookProducedVm.books.value
                val us = books.toList().filter { it.bookName == orderVm.bookName.value }
                val totalBook = us.map { it.quantityAdd }.sumOf { it }
                val orders = orderVm.order.value
                val or = orders.map { it.orderDetails }.flatMap { it }
                val orderDetails = or.toList().filter { it.bookName == orderVm.bookName.value }
                val soldBooks = orderDetails.map { it.quantity }.sumOf { it }
                val availableBook = totalBook - soldBooks
                BottomSheetScaffold(
                    modifier = Modifier.padding(15.dp),
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        BookOrder(
                            qtyAdded = orderVm.qty.value,
                            onQtyAddedChange = {
                                orderVm.qty.value = it.filter { it.isDigit() }
                            },
                            onAddClick = {
                                if (orderVm.bookName.value.isNotEmpty() && orderVm.qty.value.isNotEmpty()) {
                                    if (availableBook >= orderVm.qty.value.toInt()){
                                        val bookCheckBeforeAdding = orderVm.orderDetailsList.find { it.bookName == orderVm.bookName.value }
                                        if (bookCheckBeforeAdding == null){
                                            orderVm.addOrderDetails()
                                            orderVm.orderDetailsList
                                            to = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
//                                    state = "a"
                                            scope.launch {
                                                sheetState.collapse()
                                            }
                                        }else{
                                            snackbarVisible = true
                                            snackbarMessage = "Book exist in the list"
                                        }

                                    }else{
                                        snackbarVisible = true
                                        snackbarMessage = "Book quantity surpass the available book"

                                    }

                                } else {
                                    snackbarVisible = true
                                    snackbarMessage = "Quantity or BookName is Empty"

                                }

                            },
                            items = bookPrices,
                            onDropdownMenuItemClick = { item ->
                                orderVm.bookName.value = item.bookName
                                orderVm.priceType.value = priceSelected

                                when (priceSelected) {
                                    PRICE -> {
                                        if (item.price.isNotEmpty()) {
                                            orderVm.unitPrice.value = item.price
                                        } else {
                                            snackbarVisible = true
                                            snackbarMessage = "Price not set for this book"
                                        }
                                    }

                                    DISCOUNT_PRICE -> {
                                        if (item.discountPrice.isNotEmpty()) {
                                            orderVm.unitPrice.value = item.discountPrice
                                        } else {
                                            snackbarVisible = true
                                            snackbarMessage = "Price not set for this book"
                                        }
                                    }

                                    REP_PRICE -> {
                                        if (item.repPrice.isNotEmpty()) {
                                            orderVm.unitPrice.value = item.repPrice
                                        } else {
                                            snackbarVisible = true
                                            snackbarMessage = "Price not set for this book"
                                        }
                                    }

                                    REP_PRICE_DISCOUNT -> {
                                        if (item.repPriceDiscount.isNotEmpty()) {
                                            orderVm.unitPrice.value = item.repPriceDiscount
                                        } else {
                                            snackbarVisible = true
                                            snackbarMessage = "Price not set for this book"
                                        }
                                    }

                                    DISCOUNT_PRICE_TWO -> {
                                        if (item.priceDiscountAlt.isNotEmpty()) {
                                            orderVm.unitPrice.value = item.priceDiscountAlt
                                        } else {
                                            snackbarVisible = true
                                            snackbarMessage = "Price not set for this book"
                                        }
                                    }
                                }
                                expanded = false
                                Log.e(TAG, "priceSelected: $priceSelected", )
                            },
                            expanded = expanded,
                            bookName = orderVm.bookName.value,
                            onTextFieldClick = { expanded = true },
                            avl = availableBook.toString()
                        )
                    },
                    content = {
                        if (openDialog.value) {
                            AlertDialog(
                                onDismissRequest = {
                                    openDialog.value = false
                                },
                                shape = RoundedCornerShape(20.dp),
                                title = {
                                    Text(
                                        text = "Delete ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Are you sure you want to delete this information from the order list",
                                        style = body1
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {

                                            openDialog.value = false
                                        },
                                        shape = RoundedCornerShape(20.dp),
                                    ) {
                                        Text("No", style = TextStyle(color = Color.White))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            orderVm.orderDetailsList.remove(orderDetailsUI.value)
                                            to = orderVm.orderDetailsList.map { it.amount }
                                                .sumOf { it }
                                            openDialog.value = false
                                        }) {
                                        Text("Yes", style = TextStyle(color = Color.White))
                                    }
                                },
                                backgroundColor = Color.Blue,
                                contentColor = Color.White
                            )
                        }

                        if (priceSelected.isNotEmpty()) {
                            ContentPlaceOrder(
                                date = formattedDate,
                                customer = oneCustomer,
                                orderDetails = orderVm.orderDetailsList,
                                onOrderClick = { orderDetails ->
                                    orderDetailsUI.value = orderDetails
                                    openDialog.value = true
                                },
                                amount = to.toString(),
                                onSubmitClick = {
                                    if (orderVm.schoolName.value.isNotEmpty() && orderVm.schoolPhone.value.isNotEmpty() &&
                                        orderVm.date.value.isNotEmpty() && orderVm.paid.value.isNotEmpty() &&
                                        orderVm.totalAmount.value.isNotEmpty() && orderVm.orderDetailsList.isNotEmpty()
                                    ) {
                                        orderVm.insertOrder()
                                        snackbarVisible = true
                                        snackbarMessage = "Order details added successfully"
                                        navToHomeScreen()
                                    } else {
                                        snackbarVisible = true
                                        snackbarMessage = "Order details is not complete"
                                        state = ""
                                    }
                                    Log.e(TAG, "schoolName: ${orderVm.schoolName.value}",)
                                    Log.e(TAG, "schoolPhone: ${orderVm.schoolPhone.value}",)
                                    Log.e(TAG, "date: ${orderVm.date.value}",)
                                    Log.e(TAG, "paid: ${orderVm.paid.value}",)
                                    Log.e(TAG, "totalAmount: ${orderVm.totalAmount.value}",)
                                    Log.e(TAG, "orderDetailsList: ${orderVm.orderDetailsList}",)
                                    Log.e(TAG, "schoolPhone: ${customerListVm.oneCustomer.value}",)

                                },
                                snackbarVisible = snackbarVisible,
                                snackbarMessage = snackbarMessage,
                                snackbarAction = {
                                    snackbarVisible = false
                                },
                                swipeRefreshState = swipeRefreshState,
                                onRefreshSlide = { isRefreshing = true },
                                sheetState = sheetState
                            )
                        }
                        else {
                            OrderPaymentType(
                                onPriceClick = {
                                    priceSelected = PRICE
                                    orderVm.priceType.value = PRICE
                                },
                                onDiscountClick = {
                                    priceSelected = DISCOUNT_PRICE
                                    orderVm.priceType.value = DISCOUNT_PRICE
                                },
                                onRepClick = {
                                    priceSelected = REP_PRICE
                                    orderVm.priceType.value = REP_PRICE },
                                onRepPriceDiscountClick = {
                                    orderVm.priceType.value = REP_PRICE_DISCOUNT
                                    priceSelected = REP_PRICE_DISCOUNT },
                                onDiscountPriceTwo = {
                                    orderVm.priceType.value = DISCOUNT_PRICE_TWO
                                    priceSelected = DISCOUNT_PRICE_TWO }
                            )
                        }
                    },
                    sheetPeekHeight = 0.dp
                )

            }
        )
    }else{
            Scaffold(
                content = {
                    val pad = it
                    var oldPasswordVisibility by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 30.dp),
                            value = inputAccessKey,
                            onValueChange = {inputAccessKey=it},
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White),
                            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                            singleLine = true,
                            textStyle = button,
                            label = { Text(text = "Access Key")},
                            placeholder = { Text(text = "Access Key")},
                            trailingIcon = {
                                IconButton(onClick = {
                                    oldPasswordVisibility = !oldPasswordVisibility
                                }) {
                                    Icon(
                                        painter = if (oldPasswordVisibility) painterResource(id = R.drawable.visibility_off) else painterResource(
                                            id =R.drawable.visibility),
                                        contentDescription = "visibility",
                                        tint = Color.Black
                                    )
                                }

                            },
                            visualTransformation = if (oldPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 30.dp),
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                if (accessKeyCheck != inputAccessKey){
                                    Log.e(TAG, "UpdateBook: wrong access key", )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue, contentColor = Color.White)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Done", style = button,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            )
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContentPlaceOrder(
    date: String,
    orderDetails: List<OrderDetails>,
    onOrderClick: (OrderDetails)-> Unit,
    amount: String,
    onSubmitClick: ()-> Unit,
    customer: Customer?,
    snackbarVisible: Boolean,
    snackbarMessage: String,
    snackbarAction: ()-> Unit,
    onRefreshSlide: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    sheetState: BottomSheetState
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(snackbarVisible){
            Snackbar(
                backgroundColor = DarkBlue,
                action = {
                    Text(modifier = Modifier.clickable {
                        snackbarAction()
                    }, text = "Dismiss", style = body1)
                }) {
                Text(text = snackbarMessage, style = button)
            }
        }

        if (sheetState.isCollapsed){
            SectionTwoPlaceOrder(date = date, customer = customer)
        }

        Spacer(modifier = Modifier.padding(10.dp))
        Column(modifier = Modifier.weight(5f)) {
            SectionThreePlaceOrder(
                orderDetails = orderDetails,
                onOrderClick = onOrderClick,
                onRefreshSlide = onRefreshSlide,
                swipeRefreshState = swipeRefreshState
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        SectionFour(amount = amount, onSubmitClick = onSubmitClick)
    }
}





@Composable
fun SectionTwoPlaceOrder(
//    modifier: Modifier = Modifier,
    date: String,
    customer: Customer?,
    ){

    Column(
//        modifier
//            .fillMaxWidth() .background(Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (customer == null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth() ,
                text = date, style = body1, textAlign = TextAlign.Right
            )
        } else {

            Text(
                modifier = Modifier
                    .fillMaxWidth() ,
                text = date, style = body1, textAlign = TextAlign.Right)
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "user",
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth() ,
                text = customer.schoolName, style = h2, textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth() ,
                text = customer.address, style = button, textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth() ,
                text = customer.schoolPhoneNumber, style = button, textAlign = TextAlign.Center
            )
        }

    }
}


@Composable
fun SectionThreePlaceOrder(
    modifier: Modifier = Modifier,
    orderDetails: List<OrderDetails>,
    onOrderClick: (OrderDetails)-> Unit,
    onRefreshSlide: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
){
    Column(
        modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onRefreshSlide()},
        ) {
            if (orderDetails.isNotEmpty()){
                LazyColumn() {
                    items(items = orderDetails, ) {
                        AddOrderDetails(
                            orderDetails = it,
                            onOrderClick = { onOrderClick(it) } )
                    }
                }

            }else{
                Image(
                    painter = painterResource(id = R.drawable.empty_list),
                    contentDescription = "empty_list",
                    contentScale = ContentScale.Fit)
            }
        }



    }
    
}

@Composable
private fun AddOrderDetails(
    orderDetails: OrderDetails,
    onOrderClick: ()-> Unit
){

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .clickable { onOrderClick() },
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(modifier = Modifier
                .weight(2.5f)
                .padding(start = 10.dp),text = orderDetails.quantity.toString(), style = button)
            Text(modifier = Modifier.weight(5f),text = orderDetails.bookName, style = h2, color = DarkBlue)
            Spacer(modifier = Modifier.padding(5.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f),
            ) {
                    Text(modifier = Modifier.fillMaxWidth(),text = "₦ ${orderDetails.amount}",
                        style = button, )
                    Text(modifier = Modifier.fillMaxWidth(),text = "₦ ${orderDetails.unitPrice}",
                        style = body1,)
                }

        }
    }

}


@Composable
private fun SectionFour(
    modifier: Modifier = Modifier,
    amount: String,
    onSubmitClick: ()-> Unit
){
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = LightBlue,
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubmitClick() }
                            .weight(3f),
                        backgroundColor = DarkBlue,
                        elevation = 5.dp,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            text = "Submit",
                            style = h2,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(7f)
                            .padding(15.dp),
                        text = "Total: ₦$amount",
                        style = h2,
                        color = Color.White,
                        textAlign = TextAlign.Right
                    )


                }
            }
        }

    }



@Composable
fun OrderPaymentType(
    onPriceClick: ()-> Unit,
    onDiscountClick: ()-> Unit,
    onRepClick: ()-> Unit,
    onRepPriceDiscountClick: ()-> Unit,
    onDiscountPriceTwo: ()-> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            SelectPrice(
                onSelectPriceClick = { onPriceClick()
                    },
                price = "Price ",
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(onSelectPriceClick = {
                onDiscountClick()
                },
                price = "Discount Price"
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(
                onSelectPriceClick = { onRepClick()
                     },
                price = "Rep Price")

        Spacer(modifier = Modifier.padding(10.dp))
        SelectPrice(
                onSelectPriceClick = { onRepPriceDiscountClick()
                    },
                price = "Rep. Price Discount",
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(onSelectPriceClick = {
                onDiscountPriceTwo()
                },
                price = "Price Discount Two"
            )


    }
}

@Composable
fun SelectPrice(onSelectPriceClick: () -> Unit, price: String, ){


    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {

        Column(modifier = Modifier
//            .height(50.dp)
//            .width(100.dp)
            .clickable { onSelectPriceClick() }
            .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            androidx.compose.material3.Text(
                modifier = Modifier.padding(10.dp),
                text = price,
                style = h2,
                color = DarkBlue
            )

        }
    }
}



@Composable
fun BookOrder(
    qtyAdded: String,
    onQtyAddedChange: (String)-> Unit,
    onAddClick: ()-> Unit,
    items: List<Price>,
    onDropdownMenuItemClick: (Price)-> Unit,
    expanded: Boolean,
    bookName: String,
    onTextFieldClick: ()-> Unit,
    avl: String
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            elevation = 5.dp
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onTextFieldClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(modifier = Modifier
                    .weight(6f)
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    text = bookName, style = button, textAlign = TextAlign.Center)

                Text(modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                    text = avl, style = body1, textAlign = TextAlign.Center)
                Icon(
                    modifier = Modifier
                        .weight(2f),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "ArrowDropDown",
                    tint = DarkBlue
                )
            }
        }

        if (expanded){
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
            ) {
                items(items = items) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = 5.dp
                    ) {
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                            .clickable { onDropdownMenuItemClick(it) },
                            text = it.bookName, style = h2)
                    }

                }
            }
        }



        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
//                .padding(horizontal = 30.dp),
            value = qtyAdded,
            onValueChange = onQtyAddedChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "* Quantity")},
            placeholder = { Text(text = "Enter Quantity Add here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon")
//            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { onAddClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue, contentColor = Color.White)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Update Book Record", style = button,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }

}