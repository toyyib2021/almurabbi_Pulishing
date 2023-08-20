package com.stevdza.san.mongodemo.screen.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.data.book.BookProduced
import com.stevdza.san.mongodemo.data.order.OrderDetails
import com.stevdza.san.mongodemo.screen.homeTest.OrderVm
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h2

@Composable
fun EachBookRecord(
    onBackClick: ()-> Unit,
    bookName: String
){
    val orderVM: OrderVm = hiltViewModel()
    val orders = orderVM.order.value
    val bookProducedVm: BookRecordVM = hiltViewModel()
    val books = bookProducedVm.books.value

    val us = books.toList().filter { it.bookName == bookName }
    val totalBook = us.map { it.quantityAdd }.sumOf { it }


    val or = orders.map { it.orderDetails }.flatMap { it }
    val orderDetails = or.toList().filter { it.bookName == bookName }
    val soldBooks = orderDetails.map { it.quantity }.sumOf { it }
    val availableBook = totalBook - soldBooks



    val bookState = remember { mutableStateOf("Sold Books") }

    Scaffold(
        topBar ={
            TopBarBookList(
                onBackClick = {onBackClick() },
                priceListTitle = bookName
            )
        },
        content = {
            val i = it
            EachBookRecordContent(
                onSoldBookClick = { bookState.value = "Sold Books" },
                onProducedBookClick = { bookState.value = "" },
                sectionTwoState = bookState.value,
                orderDetails = orderDetails,
                onGetEmptyList = { /*TODO*/ },
                bookProduced = us,
                onGetEmptyListProduce = { /*TODO*/ },
                availableBook = availableBook.toString(),
                soldBooks = soldBooks.toString(),
                TotalBook = totalBook.toString(),
            )
        }
    )

}


@Composable
fun EachBookRecordContent(
    onSoldBookClick: ()-> Unit,
    onProducedBookClick: ()-> Unit,
    sectionTwoState: String,
    orderDetails: List<OrderDetails>,
    onGetEmptyList: ()-> Unit,
    bookProduced: List<BookProduced>,
    onGetEmptyListProduce: ()-> Unit,
    availableBook: String,
    soldBooks: String,
    TotalBook: String,

    ){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        SectionOne(
            modifier = Modifier.weight(0.7f),
            onSoldBookClick = {onSoldBookClick()},
            onProducedBookClick = {onProducedBookClick()}
        )
        if (sectionTwoState == "Sold Books"){
            SoldBooks(
                modifier = Modifier.weight(7f),
                orderDetails = orderDetails,
                onGetEmptyList = { onGetEmptyList() },
            )
        }else {
            Column(modifier = Modifier.weight(7f)) {
                UpdateBookContent(
                    bookProduced = bookProduced ,
                    onGetEmptyList = { onGetEmptyListProduce() },
                    onBookRecordClick = {}
                )
            }

        }
        Spacer(modifier = Modifier.padding(10.dp))
        SectionThree(
            modifier = Modifier.weight(0.6f),
            bookTitle = "AVAILABLE BOOKS",
            availableBook = availableBook)
        Spacer(modifier = Modifier.padding(5.dp))
        SectionThree(
            modifier = Modifier.weight(0.6f),
            bookTitle = "SOLD BOOKS",
            availableBook = soldBooks)
        Spacer(modifier = Modifier.padding(5.dp))
        SectionThree(
            modifier = Modifier.weight(0.6f),
            bookTitle = "TOTAL BOOKS",
            availableBook = TotalBook)
        Spacer(modifier = Modifier.padding(5.dp))
    }

}


@Composable
private fun SectionOne(
    modifier: Modifier = Modifier,
    onSoldBookClick: ()-> Unit,
    onProducedBookClick: ()-> Unit,
){

    Card(
        modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color.White,
        contentColor = DarkBlue
//        border = BorderStroke(2.dp, DarkBlue)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f)
                    .clickable { onSoldBookClick() },
                shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
//        border = BorderStroke(2.dp, DarkBlue)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "SOLD BOOKS",
                        style = h2,
                        textAlign = TextAlign.Center
                    )
                }

            }
            Column(modifier = Modifier
                .weight(5f)
                .clickable { onProducedBookClick() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "PRODUCED BOOKS",
                    style = h2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Composable
fun SoldBooks(
    modifier: Modifier = Modifier,
    orderDetails: List<OrderDetails>,
    onGetEmptyList: ()-> Unit,
    ){
    Column(
        modifier
            .fillMaxWidth()
            .padding(5.dp),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (orderDetails.isNotEmpty()){
            LazyColumn() {
                items(items = orderDetails ) {
                    OrderContentItem(
                        bookName = it.bookName,
                        date = it.date,
                        qty =it.quantity.toString() )
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
fun OrderContentItem(
    bookName: String,
    date: String,
    qty: String
){

    Column() {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = date,
            style = body1,
            textAlign = TextAlign.Left
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(8f)
                        .fillMaxWidth(),
                    text = bookName, style = h2,
                    textAlign = TextAlign.Center, color = DarkBlue)

                Card(modifier = Modifier
                    .weight(2f)
                    .padding(5.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 10.dp,
                    backgroundColor = DarkBlue,
                    contentColor = Color.White
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        text = qty,
                        style = button, textAlign = TextAlign.Center)
                }


            }
        }




    }

}


@Composable
private fun SectionThree(
    modifier: Modifier = Modifier,
    bookTitle: String,
    availableBook: String,
){

    Card(
        modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color.White,
        contentColor = DarkBlue
//        border = BorderStroke(2.dp, DarkBlue)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f) ,
                shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
//        border = BorderStroke(2.dp, DarkBlue)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bookTitle,
                        style = h2,
                        textAlign = TextAlign.Center
                    )
                }

            }
            Column(modifier = Modifier
                .weight(5f) ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = availableBook,
                    style = h2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

