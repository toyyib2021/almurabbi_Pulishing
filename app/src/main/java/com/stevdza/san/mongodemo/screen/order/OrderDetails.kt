package com.stevdza.san.mongodemo.screen.order

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.stevdza.san.mongodemo.data.order.OrderDetails
import com.stevdza.san.mongodemo.data.order.Payment
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.screen.AuthVm
import com.stevdza.san.mongodemo.screen.book.BookRecordVM
import com.stevdza.san.mongodemo.screen.book.TopBarBookList
import com.stevdza.san.mongodemo.screen.homeTest.OrderVm
import com.stevdza.san.mongodemo.screen.price.PriceVM
import com.stevdza.san.mongodemo.ui.Constants.DISCOUNT_PRICE
import com.stevdza.san.mongodemo.ui.Constants.DISCOUNT_PRICE_TWO
import com.stevdza.san.mongodemo.ui.Constants.PRICE
import com.stevdza.san.mongodemo.ui.Constants.REP_PRICE
import com.stevdza.san.mongodemo.ui.Constants.REP_PRICE_DISCOUNT
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderDetail(
    id: String, 
    onBackClick:()-> Unit,
    navToHomeScreen: ()-> Unit
){

    val orderVm: OrderVm = hiltViewModel()
    val order = orderVm.orderById
    var orderDeta = orderVm.orderDetailsList
    var payment = orderVm.paymentList
    var schoolName = orderVm.schoolName.value
    var schoolPhone = orderVm.schoolPhone.value
    var paid = orderVm.paid.value
    var totalAmount = orderVm.totalAmount.value
    var priceType = orderVm.priceType.value
    var orderDate = orderVm.orderDate.value

    val currentDate = LocalDate.now()
    val currentTime = LocalDateTime.now()
    val timeFormat = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, ''yy"))
    orderVm.date.value = formattedDate


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    order.value?.let {order1 ->
        orderDeta = order1.orderDetails
        payment = order1.payment
        schoolName = order1.schoolName
        schoolPhone = order1.schoolPhone
        paid = order1.paid.toString()
        totalAmount = order1.totalAmount.toString()
        priceType = order1.priceType
        orderDate = order1.date
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = true ){
        orderVm.objectId.value = id
        orderVm.getOrderListById()
    }

    // Payment
    var totalPayment by remember { mutableIntStateOf(0) }


    // Amount
    var priceType2 by remember { mutableStateOf("") }
    var totalAmountOrder by remember { mutableIntStateOf(0) }

    priceType2 = priceType

    if (orderVm.orderDetailsList.isEmpty()){
        orderDeta.forEach {
            orderVm.orderDetailsList.add(it)
        }
        totalAmountOrder = totalAmount.toInt()
    }

    if (orderVm.paymentList.isEmpty()){
        payment.forEach {
            orderVm.paymentList.add(it)
        }
        totalPayment = paid.toInt()
    }




    // Snackbar
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = snackbarVisible){
        if (snackbarVisible){
            delay(2000)
            snackbarVisible = false
        }
    }
    var botSheetState by remember { mutableStateOf("") }


    // Swipe to Refresh Amount
    var isRefreshingOrder by remember { mutableStateOf(false) }
    val swipeRefreshStateOrder = rememberSwipeRefreshState(isRefreshingOrder)

    LaunchedEffect(isRefreshingOrder) {
        if (isRefreshingOrder) {
            orderVm.orderDetailsList
            totalAmountOrder = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
            delay(2000L)
            isRefreshingOrder = false
        }
    }

    // Swipe to Refresh  Payment
    var isRefreshingPayment by remember { mutableStateOf(false) }
    val swipeRefreshStatePayment = rememberSwipeRefreshState(isRefreshingPayment)

    LaunchedEffect(isRefreshingPayment) {
        if (isRefreshingPayment) {
            orderVm.paidList
            totalPayment = orderVm.paymentList.map { it.paid }.sumOf { it }
            delay(2000L)
            isRefreshingPayment = false
        }
    }




    var inputAccessKey by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

    BackHandler {
        orderVm.schoolName.value = schoolName
        orderVm.schoolPhone.value = schoolPhone
        orderVm.paid.value = totalPayment.toString()
        orderVm.totalAmount.value = totalAmountOrder.toString()
        orderVm.priceType.value = priceType2
        orderVm.orderDetailsList
        orderVm.paymentList
        openDialog.value = true
        val paymentS = orderVm.paymentList.toList()
        inputAccessKey = ""

        Log.e(TAG, "schoolName: ${orderVm.schoolName.value}", )
        Log.e(TAG, "schoolPhone: ${orderVm.schoolPhone.value}", )
        Log.e(TAG, "priceType2: $priceType2", )
        Log.e(TAG, "totalAmount: ${orderVm.totalAmount.value}", )
        Log.e(TAG, "paid: ${orderVm.paid.value}", )
        Log.e(TAG, "payment: $paymentS", )
        Log.e(TAG, "priceType: $priceType", )
    }



    Scaffold(
        topBar = {
            TopBarBookList(
                onBackClick = {
                    orderVm.schoolName.value = schoolName
                    orderVm.schoolPhone.value = schoolPhone
                    orderVm.paid.value = totalPayment.toString()
                    orderVm.totalAmount.value = totalAmountOrder.toString()
                    orderVm.priceType.value = priceType2
                    orderVm.orderDetailsList
                    orderVm.paymentList
                    openDialog.value = true
                    inputAccessKey = ""
                              },
                priceListTitle = schoolName
            )
        },
        content = {
            val l = it
            var paidList = orderVm.paidList.value
             orderVm.dateList.value = formattedDate
            orderVm.modifiedDate.value = timeFormat


            val authKeyVm: AuthVm = hiltViewModel()
            val objectId by remember { mutableStateOf("64e05169576408175f7bfcca") }
            val data = authKeyVm.authById


            val sellerKeyCheck = data.value?.sellerKey
            val accessKeyCheck = data.value?.accessKey

            authKeyVm.objectId.value = objectId

            LaunchedEffect(key1 = true ){
                authKeyVm.getOrderListById()
            }


            BottomSheetScaffold(
                modifier = Modifier.padding(15.dp),
                scaffoldState = scaffoldState,
                sheetContent = {
                    val bookListVm: PriceVM = hiltViewModel()
                    val bookPrices = bookListVm.priceList.value
                    var expanded by remember { mutableStateOf(true) }
                    var passwordVisibility by remember { mutableStateOf(false) }
                    val bookProducedVm: BookRecordVM = hiltViewModel()
                    val books = bookProducedVm.books.value
                    val us = books.toList().filter { it.bookName == orderVm.bookName.value }
                    val totalBook = us.map { it.quantityAdd }.sumOf { it }
                    val orders = orderVm.order.value
                    val or = orders.map { it.orderDetails }.flatMap { it }
                    val orderDetails = or.toList().filter { it.bookName == orderVm.bookName.value }
                    val soldBooks = orderDetails.map { it.quantity }.sumOf { it }
                    val availableBook = totalBook - soldBooks
                    if (botSheetState == "order"){
                       UpdateOrderDetails(
                           qtyAdded = orderVm.qty.value,
                           onQtyAddedChange = {
                               orderVm.updateQty(it.filter { it.isDigit() })
                           },
                           onAddClick = {
                               if (sellerKeyCheck == inputAccessKey){
                                   if (orderVm.bookName.value.isNotEmpty() && orderVm.qty.value.isNotEmpty()
                                   ){
                                       if (availableBook >= orderVm.qty.value.toInt()){
                                           val d = orderVm.orderDetailsList.find { it.bookName == orderVm.bookName.value }
                                           if (d == null){
                                               orderVm.addOrderDetails()
                                               orderVm.paymentList
                                               totalPayment = orderVm.paymentList.map { it.paid }.sumOf { it }
                                               orderVm.orderDetailsList
                                               totalAmountOrder = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
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
                                   }else{
                                       snackbarVisible = true
                                       snackbarMessage = "Quantity or BookName is Empty"

                                   }
                               }else{
                                   snackbarVisible = true
                                   snackbarMessage = "Wrong Access Key"
                               }

                           },
                           items = bookPrices ,
                           onDropdownMenuItemClick ={item ->
                               orderVm.bookName.value = item.bookName
                               when(priceType2){

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
                           } ,
                           expanded = expanded,
                           onTextFieldClick = { expanded = true },
                           bookName = orderVm.bookName.value,
                           botSheetState = botSheetState,
                           placeHolderText = "Enter the qty here",
                           lableText = "Quantity",
                           accessKey = inputAccessKey,
                           onAccessKeyChange = {inputAccessKey=it},
                           passwordVisibility = passwordVisibility,
                           onTrailingIconClick = {passwordVisibility = !passwordVisibility},
                           bookAvl = availableBook.toString()
                       )
                    }else if (botSheetState == "payment") {
                        UpdateOrderDetails(
                            qtyAdded = paidList,
                            onQtyAddedChange = {
                                orderVm.updatePaidList(it.filter { it.isDigit() })
                            },
                            onAddClick = {
                                if (accessKeyCheck == inputAccessKey){
                                    if (orderVm.dateList.value.isNotEmpty() && orderVm.paidList.value.isNotEmpty()
                                        && orderVm.modifiedDate.value.isNotEmpty()
                                    ){
                                        if ((totalAmountOrder - totalPayment) >= paidList.toInt()){
                                            orderVm.addPayment()
                                            orderVm.paymentList
                                            totalPayment = orderVm.paymentList.map { it.paid }.sumOf { it }
                                            scope.launch {
                                                sheetState.collapse()
                                            }
                                        }else{
                                            snackbarVisible = true
                                            snackbarMessage = "Check Payment Balance"
                                        }

                                    }else {
                                        snackbarVisible = true
                                        snackbarMessage = "You did did not enter any amount"
                                    }
                                }else {
                                    snackbarVisible = true
                                    snackbarMessage = "Wrong Access Key"
                                }
                            },
                            placeHolderText = "Enter the amount here",
                            lableText = "Amount",
                            items = emptyList(),
                            onDropdownMenuItemClick = {},
                            expanded = expanded,
                            onTextFieldClick = {},
                            bookName = "",
                            botSheetState = "",
                            accessKey = inputAccessKey,
                            onAccessKeyChange = {inputAccessKey=it},
                            passwordVisibility = passwordVisibility,
                            onTrailingIconClick = {passwordVisibility = !passwordVisibility},
                            bookAvl = ""
                        )
                    }

                },
                content = {
                    var deletePayment by remember { mutableStateOf<Payment?>(null) }
                    var deleteOrder by remember { mutableStateOf<OrderDetails?>(null) }

                    ContentOrderDetails(
                                date = orderDate,
                                orderDetails = orderVm.orderDetailsList,
                                onOrderClick = {
                                    inputAccessKey = ""
                                    deleteOrder = it
                                    openDialog.value= true
                                },
                                snackbarVisible = snackbarVisible,
                                snackbarMessage = snackbarMessage,
                                snackbarAction = { snackbarVisible = false },
                                payment = orderVm.paymentList,
                                onPaymentClick = {
                                    inputAccessKey = ""
                                    deletePayment = it
                                    openDialog.value = true
                                },
                                schoolName = schoolName,
                                schoolPhoneNumber = schoolPhone,
                                onAddClick = {
                                    inputAccessKey = ""
                                    paidList = (totalAmountOrder - totalPayment).toString()
                                    botSheetState = "payment"
                                    orderVm.updatePaidList((totalAmountOrder - totalPayment).toString())
                                    scope.launch {
                                        if (sheetState.isCollapsed) {
                                            sheetState.expand()
                                        } else {
                                            sheetState.collapse()
                                        }
                                    }
                                },
                                total = totalAmountOrder.toString(),
                                balance = (totalAmountOrder - totalPayment).toString(),
                                paid = totalPayment.toString(),
                                onAddCOrderlick = {
                                    inputAccessKey = ""
                                    botSheetState = "order"
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
                                onRefreshSlideOrder = {isRefreshingOrder = true},
                                swipeRefreshStateOrder = swipeRefreshStateOrder,
                                onRefreshSlidePayment = {isRefreshingPayment = true},
                                swipeRefreshStatePayment = swipeRefreshStatePayment,
                                openDialog = openDialog,
                                onYesDeleteClick = {
                                    if (deleteOrder != null ){

                                        if (sellerKeyCheck == inputAccessKey){
                                            orderVm.orderDetailsList.remove(deleteOrder)
                                            deleteOrder = null
                                            totalAmountOrder = orderVm.orderDetailsList.map { it.amount }.sumOf { it }
                                        }else{
                                            snackbarVisible = true
                                            snackbarMessage = "Wrong Access Key"
                                        }

                                    }else if (deletePayment != null){
                                        if (accessKeyCheck == inputAccessKey){
                                            orderVm.paymentList.remove(deletePayment)
                                            deletePayment = null
                                            totalPayment = orderVm.paymentList.map { it.paid }.sumOf { it }
                                        }else{
                                            snackbarVisible = true
                                            snackbarMessage = "Wrong Access Key"
                                        }

                                    }else{
                                        if (accessKeyCheck == inputAccessKey){
                                            orderVm.updateOrder()
                                            navToHomeScreen()
                                        }else if (sellerKeyCheck == inputAccessKey){
                                            orderVm.updateOrder()
                                            navToHomeScreen()
                                        }else{
                                            snackbarVisible = true
                                            snackbarMessage = "Wrong Access Key"
                                        }

                                    }

                                },
                                dialogTitle = if (deleteOrder != null){
                                    "Delete Order"
                                }else if (deletePayment != null){
                                    "Delete Payment"
                                }else{
                                    "Close Order"
                                },
                                dialogDes =if (deleteOrder != null){
                                    "Are you sure you want to delete  ${deleteOrder?.quantity} ${deleteOrder?.bookName},"
                                }else if (deletePayment != null){
                                    "Are you sure you want to delete payment of ${deletePayment?.paid}"
                                }else{
                                    "Are you sure you want to close order for $schoolName"
                                },
                                onNoDeleteClick = {
                                    deleteOrder = null
                                    deletePayment = null
                                },
                            bottomSheetState = sheetState,
                        accessKey = inputAccessKey,
                        onAccessKeyChange = {inputAccessKey=it}
                            )

                        Log.e(TAG, "totalAmountOrder: $totalAmountOrder", )

                },
                sheetPeekHeight = 0.dp

            )
        }
    )

}




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContentOrderDetails(
    date: String,
    orderDetails: List<OrderDetails>,
    onOrderClick: (OrderDetails)-> Unit,
    snackbarVisible: Boolean,
    snackbarMessage: String,
    snackbarAction: ()-> Unit,
    payment: List<Payment>,
    onPaymentClick: (Payment)-> Unit,
    schoolPhoneNumber: String,
    schoolName: String,
    onAddClick: ()-> Unit,
    total: String,
    balance: String,
    paid: String,
    onAddCOrderlick:()-> Unit,
    onRefreshSlidePayment: () -> Unit,
    swipeRefreshStatePayment: SwipeRefreshState,
    onRefreshSlideOrder: () -> Unit,
    swipeRefreshStateOrder: SwipeRefreshState,
    openDialog: MutableState<Boolean>,
    onYesDeleteClick:()-> Unit,
    dialogTitle: String,
    dialogDes: String,
    onNoDeleteClick: ()-> Unit,
    bottomSheetState: BottomSheetState,
    accessKey: String,
    onAccessKeyChange: (String)-> Unit
    ){
    Column(modifier = Modifier
//        .fillMaxSize()
        .padding(5.dp)
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var passwordVisibilityAlertDiallog by remember { mutableStateOf(false) }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    onNoDeleteClick()
                    openDialog.value = false
                },
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(text = dialogTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    Column() {
                        Text(text = dialogDes, style = body1)
                        Spacer(modifier = Modifier.padding(7.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 30.dp),
                            value = accessKey,
                            onValueChange = {onAccessKeyChange(it)},
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White, textColor = DarkBlue),
                            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                            singleLine = true,
                            textStyle = button,
//                label = { Text(text = "Access Key")},
                            placeholder = { Text(text = "Access Key")},
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisibilityAlertDiallog = !passwordVisibilityAlertDiallog
                                }) {
                                    Icon(
                                        painter = if (passwordVisibilityAlertDiallog) painterResource(id = R.drawable.visibility_off) else painterResource(
                                            id = R.drawable.visibility
                                        ),
                                        contentDescription = "visibility",
                                        tint = Color.Black
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisibilityAlertDiallog) VisualTransformation.None else PasswordVisualTransformation(),
                        )
                    }

                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onNoDeleteClick()
                            openDialog.value = false
                        },
                        shape =  RoundedCornerShape(20.dp),
                    ) {
                        Text("No",style = TextStyle(color = Color.White))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onYesDeleteClick()
                            openDialog.value = false
                        }) {
                        Text("Yes",style = TextStyle(color = Color.White))
                    }
                },
                backgroundColor = DarkBlue,
                contentColor = Color.White
            )
        }


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

        if (bottomSheetState.isCollapsed){
            Column(modifier = Modifier.weight(1f)) {
                BalTotalAndPaid(
                    total = total,
                    balance = balance,
                    paid = paid
                )
            }

            Column(modifier = Modifier.weight(2f)) {
                CustomerDetails(date = date, schoolName = schoolName, schoolPhoneNumber = schoolPhoneNumber)
            }
        }

            Spacer(modifier = Modifier.padding(5.dp))
            Column(modifier = Modifier.weight(4f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .weight(8f),
                        text = "Order Details",
                        style = button
                    )

                    Icon(
                        modifier = Modifier
                            .weight(2f)
                            .clickable {
                                onAddCOrderlick()
                            },
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = DarkBlue,
                    )

                }

                SectionThreePlaceOrder(
                    orderDetails = orderDetails,
                    onOrderClick = onOrderClick,
                    swipeRefreshState = swipeRefreshStateOrder,
                    onRefreshSlide =onRefreshSlideOrder
                )
            }

        Spacer(modifier = Modifier.padding(10.dp))
        Column(modifier = Modifier.weight(3f)) {
            PaymentContent(
                payment = payment,
                onPaymentClick = { onPaymentClick(it) },
                onAddClick = {onAddClick()},
                swipeRefreshState = swipeRefreshStatePayment,
                onRefreshSlide = onRefreshSlidePayment
            )

        }

    }
}
@Composable
fun BalTotalAndPaid(
    total: String,
    balance: String,
    paid: String
){

    Card(modifier = Modifier.fillMaxSize(), elevation = 5.dp) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        ) {
            Card(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize(),
            ) {
                Column() {
                    Text(
                        text = "Total", style = button,
                        modifier = Modifier
                            .background(DarkBlue)
                            .fillMaxWidth()
                            .padding(7.dp),
                        color = Color.White
                    )
//                Spacer(modifier = Modifier.padding(5.dp))
                    Text(modifier = Modifier.padding(horizontal = 7.dp),
                        text = "₦ $total",
                        style = button,
                        color = DarkBlue,
                        textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Card(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize(),
                shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                backgroundColor = DarkBlue
            ) {
                Column() {
                    Text(
                        text = "Balance", style = button,
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(7.dp),
                        color = DarkBlue
                    )
//                Spacer(modifier = Modifier.padding(5.dp))
                    Text(modifier = Modifier.padding(3.dp),
                        text = "₦ $balance",
                        style = button,
                        color = Color.White
                        ,
                        textAlign = TextAlign.Center)
                }

            }
            Spacer(modifier = Modifier.padding(2.dp))
            Card(modifier = Modifier
                .weight(3f)
                .fillMaxSize()) {

                Column() {
                    Text(
                        text = "Paid", style = button,
                        modifier = Modifier
                            .background(DarkBlue)
                            .fillMaxWidth()
                            .padding(7.dp),
                        color = Color.White
                    )
//                Spacer(modifier = Modifier.padding(5.dp))
                    Text(modifier = Modifier.padding(3.dp),
                        text = "₦ $paid",
                        style = button,
                        color = DarkBlue,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }


}

@Composable
fun CustomerDetails(
//    modifier: Modifier = Modifier,
    date: String,
    schoolPhoneNumber: String,
    schoolName: String

){

    Column(
//        modifier
//            .fillMaxWidth() .background(Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
            text = schoolName, style = h2, textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth() ,
            text =schoolPhoneNumber, style = button, textAlign = TextAlign.Center
        )
    }
}


@Composable
fun PaymentContent(
    payment: List<Payment>,
    onPaymentClick: (Payment)-> Unit,
    onAddClick: ()-> Unit,
    onRefreshSlide: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(8f),
                text = "Payment History",
                style = button
            )

            Icon(
                modifier = Modifier
                    .weight(2f)
                    .clickable {
                        onAddClick()
                    },
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = DarkBlue,
            )

        }
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onRefreshSlide()},
        ) {
            if (payment.isNotEmpty()){
                LazyColumn(modifier = Modifier.fillMaxWidth() ){
                    items(items = payment) {
                        PaymentDetails(payment = it,
                            onPaymentClick = { onPaymentClick(it) },
                        )

                    }
                }
            }else{
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.empty_list),
                    contentDescription = "empty_list",
                    contentScale = ContentScale.Fit
                )
            }
        }


    }
}

@Composable
fun PaymentDetails(payment: Payment , onPaymentClick: ()-> Unit){

    Column(modifier = Modifier.padding(vertical = 10.dp)) {

        Card(
            shape = RoundedCornerShape(5.dp),
            elevation = 7.dp
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onPaymentClick() }
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(5f)) {
                    Text( text = payment.date, style = button)
                    Text(text = payment.updateDate, style = body1)

                }
                Card(modifier = Modifier
                    .weight(5f)
                    .fillMaxSize(),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = DarkBlue,
                    elevation = 7.dp,
                    contentColor = Color.White
                ) {
                    Text(modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                        text = "₦ ${payment.paid}", style = h2, textAlign = TextAlign.Center)
                }
            }
        }
    }

}


@Composable
private fun UpdateOrderDetails(
    qtyAdded: String,
    onQtyAddedChange: (String)-> Unit,
    onAddClick: ()-> Unit,
    placeHolderText: String = "",
    lableText: String = "",
    items: List<Price>,
    onDropdownMenuItemClick: (Price)-> Unit,
    expanded: Boolean,
    onTextFieldClick: ()-> Unit,
    bookName: String,
    botSheetState: String,
    onAccessKeyChange: (String)-> Unit,
    accessKey: String,
    passwordVisibility: Boolean,
    onTrailingIconClick:()-> Unit,
    bookAvl: String
    ){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        elevation = 5.dp,
        backgroundColor = DarkBlue,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (botSheetState == "order"){
                Spacer(modifier = Modifier.padding(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
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
                            text = bookAvl, style = body1, textAlign = TextAlign.Center)
                        Icon(
                            modifier = Modifier
                                .weight(2f),
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "ArrowDropDown",
                            tint = DarkBlue
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            if (expanded){
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
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

            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 30.dp),
                value = qtyAdded,
                onValueChange = onQtyAddedChange,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
//            label = { Text(text = lableText)},
                placeholder = { Text(text = placeHolderText)},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon")
//            },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 30.dp),
                value = accessKey,
                onValueChange = {onAccessKeyChange(it)},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
//                label = { Text(text = "Access Key")},
                placeholder = { Text(text = "Access Key")},
                trailingIcon = {
                    IconButton(onClick = {
                        onTrailingIconClick()
                    }) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.visibility_off) else painterResource(
                                id = R.drawable.visibility
                            ),
                            contentDescription = "visibility",
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.padding(5.dp))

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
                    text = "Done", style = button,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}


