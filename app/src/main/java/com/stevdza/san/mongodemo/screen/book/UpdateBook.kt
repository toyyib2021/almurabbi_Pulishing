package com.stevdza.san.mongodemo.screen.book

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.data.book.BookProduced
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.screen.AuthVm
import com.stevdza.san.mongodemo.screen.price.PriceVM
import com.stevdza.san.mongodemo.screen.price.TopBarPriceCustomer
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateBook(onBackClick: ()-> Unit){

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bookProducedVm: BookRecordVM = hiltViewModel()
    val books = bookProducedVm.books.value
    val bookListVm: PriceVM = hiltViewModel()
    val bookPrices = bookListVm.priceList.value

    Log.e(TAG, "UpdateBook: ${bookPrices}", )
    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, ''yy"))
    bookProducedVm.date.value = formattedDate
    var bookName = bookProducedVm.bookName.value
    val bookQty = bookProducedVm.bookQty.value
    var datetest = bookProducedVm.date.value
    val bookNames: List<String> = bookPrices.map { it.bookName }
    var expanded by remember { mutableStateOf(true) }
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = snackbarVisible){
        if (snackbarVisible){
            delay(2000)
            snackbarVisible = false
        }
    }

    BackHandler() { onBackClick() }

    val authKeyVm: AuthVm = hiltViewModel()
    val objectId by remember { mutableStateOf("64e05169576408175f7bfcca") }
    val data = authKeyVm.authById


    val accessKeyCheck = data.value?.accessKey
    var inputAccessKey by remember { mutableStateOf("") }

    authKeyVm.objectId.value = objectId

    LaunchedEffect(key1 = true ){
        authKeyVm.getOrderListById()
    }
    
    if (accessKeyCheck == inputAccessKey){
        Scaffold(
            topBar = {
                TopBarPriceCustomer(
                    onBackClick = { onBackClick() },
                    onAddClick = {
                        bookProducedVm.objectId.value = ""
                        bookProducedVm.bookName.value = ""
                        bookProducedVm.bookQty.value = ""
                        scope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    },
                    priceListTitle = "Book Produced"
                )

            },
            content = {
                var bottomsheetName by remember { mutableStateOf("") }
                var bottomsheetQty by remember { mutableStateOf("") }
                var date by remember { mutableStateOf("") }
                bookProducedVm.uBookName.value = bottomsheetName
                bookProducedVm.uBookQty.value = bottomsheetQty
                bookProducedVm.uDate.value = date



                BottomSheetScaffold(
                    modifier = Modifier.padding(it),
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        if (bookProducedVm.objectId.value == ""){

                            BookProduceUpdate(
                                qtyAdded = bookProducedVm.bookQty.value,
                                onQtyAddedChange = bookProducedVm::updatbookQty,
                                onAddClick = {
                                    if (bookProducedVm.bookName.value.isNotEmpty() && bookProducedVm.bookQty.value.isNotEmpty()){
                                        bookProducedVm.insertBookRecord()
                                        scope.launch {
                                            sheetState.collapse()
                                        }
                                    }else {
                                        snackbarVisible = true
                                        snackbarMessage = "some fields are empty"
                                    }
                                },
                                items = bookPrices,
                                onDropdownMenuItemClick = {item ->
                                    bookProducedVm.bookName.value = item.bookName
                                    expanded = false
                                },
                                expanded = expanded,
                                bookName = bookProducedVm.bookName.value,
                                onTextFieldClick = {expanded = true}
                            )
                            Log.e(TAG, "UpdateBook 1: $bookName", )
                            Log.e(TAG, "UpdateBook 2: $bookQty", )
                            Log.e(TAG, "UpdateBook 3: $datetest", )
                        }else{


                            BookProduceUpdate(
                                qtyAdded = bottomsheetQty,
                                onQtyAddedChange = {
                                    bottomsheetQty = it
                                },
                                onAddClick = {
                                    if (bookProducedVm.bookName.value.isEmpty() && bookProducedVm.bookQty.value.isEmpty()){
                                        snackbarVisible = true
                                        snackbarMessage = "some fields are empty"
                                    }else {
                                        bookProducedVm.updateBookRecord()
//                                    bookProducedVm.deletePerson()
                                        scope.launch {
                                            sheetState.collapse()
                                        }
                                    }
                                },
                                items = bookPrices,
                                onDropdownMenuItemClick = {item ->
                                    bottomsheetName = item.bookName
                                    expanded = false
                                },
                                expanded = expanded,
                                bookName = bottomsheetName,
                                onTextFieldClick = {expanded = true}
                            )
                        }

                    },

                    content = {

                        UpdateBookContent(
                            bookProduced = books.sortedByDescending { it._id },
                            onGetEmptyList = { bookProducedVm.fechtAllBookRecord()},
                            onBookRecordClick = {bookProduced ->
                                bookProducedVm.objectId.value = bookProduced._id.toHexString()
                                bottomsheetName = bookProduced.bookName
                                bottomsheetQty = bookProduced.quantityAdd.toString()
                                date = bookProduced.date
                                expanded = false
                                scope.launch {
                                    if (sheetState.isCollapsed) {
                                        sheetState.expand()
                                    } else {
                                        sheetState.collapse()
                                    }
                                }
                            },
                            snackbarVisible = snackbarVisible,
                            onActionCLick = {
                                snackbarVisible = false
                            },
                            snackbarMessage = snackbarMessage
                        )
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



@Composable
fun UpdateBookContent(
    bookProduced: List<BookProduced>,
    onGetEmptyList: ()-> Unit,
    onBookRecordClick: (BookProduced)-> Unit,
    snackbarVisible: Boolean = false,
    onActionCLick: ()-> Unit = {},
    snackbarMessage: String = ""


    ){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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

        if (bookProduced.isNotEmpty()){
            LazyColumn() {
                items(items = bookProduced, key = { it._id.toHexString() }) {
                    BookContentItem(bookProduced = it, onBookRecordClick = { onBookRecordClick(it)})
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
@Composable
fun BookContentItem(
    bookProduced: BookProduced,
    onBookRecordClick: ()-> Unit
){


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onBookRecordClick() },
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6.5f)
                        .padding(10.dp)
                        .fillMaxWidth(),
                    text = bookProduced.bookName, style = h2,
                    textAlign = TextAlign.Left,
                    color = DarkBlue
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(3.5f),) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        shape = RoundedCornerShape(3.dp),
                        backgroundColor = DarkBlue,
                        contentColor = Color.White
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            text = bookProduced.quantityAdd.toString(),
                            style = button, textAlign = TextAlign.Center
                        )
                }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        text = bookProduced.date,
                        style = body1,
                        textAlign = TextAlign.Left
                    )
            }
        }
    }
}



@Composable
fun BookProduceUpdate(
    qtyAdded: String,
    onQtyAddedChange: (String)-> Unit,
    onAddClick: ()-> Unit,
    items: List<Price>,
    onDropdownMenuItemClick: (Price)-> Unit,
    expanded: Boolean,
    bookName: String,
    onTextFieldClick: ()-> Unit
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
                .clickable { onTextFieldClick() })
            {

                Text(modifier = Modifier
                    .weight(8f)
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    text = bookName, style = h2, textAlign = TextAlign.Center)
                Icon(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
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



@Composable
fun SpinnerList(
    items: List<String>,
    selectedItem: String,
    onDropdownMenuItemClick: (String)-> Unit) {
    var expanded by remember { mutableStateOf(true) }
//    var selectedItem by remember { mutableStateOf(items[0]) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(modifier = Modifier.clickable { expanded = true }, text = selectedItem)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        onDropdownMenuItemClick(items[0])
                        expanded = false
                    }) {
                        
                        Text(text = item)
                    }

            }

        }
    }
}