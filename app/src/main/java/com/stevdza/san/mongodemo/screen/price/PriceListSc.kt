package com.stevdza.san.mongodemo.screen.price

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevdza.san.mongodemo.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.ui.theme.Cyan
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceList(onBackClick: ()-> Unit){

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val priceVm: PriceVM = hiltViewModel()
    val openDialog = remember { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            priceVm.getPriceList()
            delay(2000L)
            isRefreshing = false
        }
    }

    val addOrUpdate = priceVm.addOrUpdate.value



    val bookName = priceVm.bookName.value
    val price = priceVm.price.value
    val discount = priceVm.discountPrice.value
    val rep = priceVm.repPrice.value
    val repPriceDiscount = priceVm.repPriceDiscount.value
    val discountPriceTwo = priceVm.discountPriceTwo.value

    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = snackbarVisible){
        if (snackbarVisible){
            delay(2000)
            snackbarVisible = false
        }
    }
    BackHandler() { onBackClick() }

    Scaffold(
        topBar = {
                 TopBarPriceCustomer(
                     onBackClick = { onBackClick() },
                     onAddClick = {
                         priceVm.addOrUpdate.value = "Add Price"
                         priceVm.addAndUpdate("")
                         priceVm.objectId.value = ""
                         priceVm.updateBookName("")
                         priceVm.updatePrice("")
                         priceVm.updateDiscountPrice("")
                         priceVm.updateRepPrice("")
                         priceVm.updatePriceDiscountTwo("")
                         priceVm.updateRepPriceDiscount("")
                         scope.launch {
                             if (sheetState.isCollapsed) {
                                 sheetState.expand()
                             } else {
                                 sheetState.collapse()
                             }
                         }
                     },
                     priceListTitle = "Price List"
                 )
            },
        content = {
            BottomSheetScaffold(
                modifier = Modifier.padding(it),
                scaffoldState = scaffoldState,
                sheetContent = {
                    BottomSheetPriceList(
                        bookName = bookName,
                        price = price,
                        discount = discount,
                        rep = rep,
                        onPriceChange = { priceVm.updatePrice(it.filter { it.isDigit() }) },
                        onBookNameChange = { priceVm.updateBookName(it) },
                        onDiscountChange = { priceVm.updateDiscountPrice(it.filter { it.isDigit()}) },
                        onRepChange = { priceVm.updateRepPrice(it.filter { it.isDigit()}) },
                        onAddClick = {
                            if (priceVm.bookName.value.isNotEmpty() && priceVm.price.value.isNotEmpty()
                                && priceVm.discountPrice.value.isNotEmpty() && priceVm.repPrice.value.isNotEmpty()
//                                && priceVm.discountPriceTwo.value.isNotEmpty() && priceVm.repPriceDiscount.value.isNotEmpty()
                            ){
                                when(addOrUpdate){
                                    "Add Price" -> {
                                        val matchingBookName = priceVm.priceList.value.find { it.bookName == priceVm.bookName.value }
                                        if (matchingBookName == null){
                                            priceVm.insertPrice()
                                        }else{
                                            snackbarMessage = "Book Already Exist"
                                            snackbarVisible = true
                                        }

                                    }
                                    "Edit Price" -> {
                                            priceVm.updatePriceList()
                                    }
                                }
                                scope.launch {
                                    sheetState.collapse()
                                }
                            }else{
                                snackbarVisible = true
                                snackbarMessage = "some fields are empty"
                            }

                        },
                        addOrUpdate = priceVm.addOrUpdate.value,
                        repDiscount = repPriceDiscount,
                        discountPriceTwo = discountPriceTwo,
                        onPriceDiscountTwoChange = { priceVm.updatePriceDiscountTwo(it.filter { it.isDigit() }) },
                        onRepDiscountChange = {priceVm.updateRepPriceDiscount(it.filter { it.isDigit() })}
                    )
                },
                sheetPeekHeight = 0.dp
            ){
                PriceListContent(
                    onRefreshSlide = { isRefreshing = true },
                    priceList = priceVm.priceList.value,
                    swipeRefreshState = swipeRefreshState,
                    openDialog = openDialog ,
                    onDeleteClick = {it2 ->
                        priceVm.objectId.value = it2._id.toHexString()
                        priceVm.updateBookName(it2.bookName)
                        openDialog.value = true },
                    onYesDeleteClick = {
                        priceVm.deleteBook() },
                    onEditClick = { it1 ->
                        priceVm.objectId.value = it1._id.toHexString()
                        priceVm.updateBookName(it1.bookName)
                        priceVm.updatePrice(it1.price)
                        priceVm.updateDiscountPrice(it1.discountPrice)
                        priceVm.updateRepPrice(it1.repPrice)
                        priceVm.updateRepPriceDiscount(it1.repPriceDiscount)
                        priceVm.updatePriceDiscountTwo(it1.priceDiscountAlt)
                        priceVm.addOrUpdate.value = "Edit Price"
                        Log.e(TAG, "PriceList: $bookName", )
                        scope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    },
                    bookName = bookName,
                    onGetEmptyList = {
                        priceVm.getPriceList()
                    },
                    snackbarMessage = snackbarMessage,
                    snackbarVisible = snackbarVisible,
                    onActionCLick = {snackbarVisible = false}
                )
            }

        }
    )



}



@Composable
fun PriceListContent(
    onRefreshSlide: () -> Unit,
    priceList: List<Price>,
    swipeRefreshState: SwipeRefreshState,
    openDialog: MutableState<Boolean>,
    onDeleteClick: (Price)-> Unit,
    onYesDeleteClick: ()-> Unit,
    onGetEmptyList: ()-> Unit,
    onEditClick: (Price)-> Unit,
    bookName: String,
    snackbarVisible: Boolean,
    snackbarMessage: String,
    onActionCLick: ()-> Unit

){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(text = "Delete User", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    Column() {
                        Text(text = "Are you sure you want to Delete $bookName and it Prices",
                            style = body1)
                    }

                },
                confirmButton = {
                    TextButton(
                        onClick = {
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
                        onActionCLick()
                    }, text = "Dismiss", style = body1)
                }) {
                Text(text = snackbarMessage, style = button)
            }
        }


        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onRefreshSlide()},
        ) {
            if (priceList.isNotEmpty()){
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items = priceList, key = { it._id.toHexString() }) {
                        PriceListWidget(
                            priceList = it,
                            onDeleteClick = {onDeleteClick(it)},
                            onEditClick = { onEditClick(it) }
                        )
                    }

                }
            }else{
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onGetEmptyList() },
                    painter = painterResource(id = R.drawable.empty_list),
                    contentDescription = "empty_list",
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}



@Composable
fun PriceListWidget(
    priceList: Price,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Cyan,
            elevation = 10.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = DarkBlue
                ) {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .weight(6f)
                                .fillMaxWidth(),
                            text = priceList.bookName,
                            textAlign = TextAlign.Left,
                            style = button,
                            color = Color.White

                        )
                        Icon(
                            modifier = Modifier
                                .weight(2f)
                                .clickable { onEditClick() },
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                        Icon(
                            modifier = Modifier
                                .weight(2f)
                                .clickable { onDeleteClick() },
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )

                    }
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                         Card(
                             modifier = Modifier
                                 .weight(5f)
                                 .fillMaxSize(),
                             contentColor = DarkBlue,
                             elevation = 5.dp
                         ) {
                             Text(modifier = Modifier
                                 .fillMaxSize().padding(5.dp),
                                 text = "Price: ${priceList.price}", style = button, textAlign = TextAlign.Center)
                         }
                         Spacer(modifier = Modifier.padding(10.dp))
                         Card(
                             modifier = Modifier
                                 .weight(5f)
                                 .fillMaxSize(),
                             contentColor = DarkBlue,
                             elevation = 5.dp
                         ) {
                             Text(modifier = Modifier
                                 .fillMaxSize().padding(5.dp),
                                 text = "Discount Price: ${priceList.discountPrice}", style = button, textAlign = TextAlign.Center)
                         }
                     }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(modifier = Modifier.fillMaxSize()) {
                            Card(
                             modifier = Modifier
                                 .fillMaxSize(),
                             contentColor = DarkBlue,
                             elevation = 5.dp
                             ) {
                             Text(modifier = Modifier
                                 .fillMaxSize().padding(5.dp),
                                 text = "Rep. Price: ${priceList.repPrice}", style = button, textAlign = TextAlign.Center)
                            }
                         }
                        Spacer(modifier = Modifier.padding(5.dp))
                       if (priceList.repPriceDiscount.isNotEmpty()){
                           Row(modifier = Modifier.fillMaxSize()) {
                               Card(
                                   modifier = Modifier
                                       .fillMaxSize() ,
                                   contentColor = DarkBlue,
                                   elevation = 5.dp
                               ) {
                                   Text(modifier = Modifier
                                       .fillMaxSize().padding(5.dp),
                                       text = "Rep. Discount Price: ${priceList.repPriceDiscount}", style = button, textAlign = TextAlign.Center)
                               }
                           }

                       }
                        Spacer(modifier = Modifier.padding(5.dp))
                        if (priceList.priceDiscountAlt.isNotEmpty()){
                            Row(modifier = Modifier.fillMaxSize()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    contentColor = DarkBlue,
                                    elevation = 5.dp
                                ) {
                                    Text(modifier = Modifier
                                        .fillMaxSize().padding(5.dp),
                                        text = "Discount Price Two: ${priceList.priceDiscountAlt}", style = button, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }

                }


            }

        }

    }
}




@Composable
fun TopBarPriceCustomer(
    onBackClick: ()-> Unit,
    onAddClick: ()-> Unit,
    priceListTitle: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {

        Icon(
            modifier = Modifier
                .weight(2f)
                .clickable { onBackClick() },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "ArrowBack",
            tint = DarkBlue
        )
        Text(
            modifier = Modifier
                .weight(6f)
                .fillMaxWidth(),
            text = priceListTitle,
            style = h1,
            textAlign = TextAlign.Center,
            color = DarkBlue
        )
        Icon(
            modifier = Modifier
                .weight(2f)
                .clickable {
                    onAddClick()
                },
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
            tint = DarkBlue
        )
    }
}


@Composable
fun BottomSheetPriceList(
    bookName: String,
    price: String,
    discount: String,
    rep: String,
    repDiscount: String,
    discountPriceTwo: String,
    onPriceChange: (String) -> Unit,
    onBookNameChange: (String) -> Unit,
    onDiscountChange: (String) -> Unit,
    onRepChange: (String) -> Unit,
    onRepDiscountChange: (String) -> Unit,
    onPriceDiscountTwoChange: (String) -> Unit,
    onAddClick: () -> Unit,
    addOrUpdate: String
){

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {


        if (addOrUpdate == "Add Price"){
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp),
                value = bookName,
                onValueChange = onBookNameChange ,
                colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
                label = { Text(text = "Book Name")},
                placeholder = { Text(text = "Enter your Book Name here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Menu, contentDescription = "emailIcon") },
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = price,
            onValueChange = onPriceChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Price")},
            placeholder = { Text(text = "Enter your price here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon")
//            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = discount,
            onValueChange = onDiscountChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Discount Price")},
            placeholder = { Text(text = "Enter your discount price here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = rep,
            onValueChange = onRepChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Rep Price")},
            placeholder = { Text(text = "Enter your Rep Price here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = repDiscount,
            onValueChange = onRepDiscountChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Rep Price Discount")},
            placeholder = { Text(text = "Enter discount Rep Price here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = discountPriceTwo,
            onValueChange = onPriceDiscountTwoChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Rep Price Discount Two")},
            placeholder = { Text(text = "Enter discount Rep Price Two here")},
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
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
                text = addOrUpdate, style = button,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }


}