package com.stevdza.san.mongodemo.screen.book

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdza.san.mongodemo.screen.homeTest.OrderVm
import com.stevdza.san.mongodemo.screen.price.PriceVM
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.h1
import com.stevdza.san.mongodemo.ui.theme.h2

@Composable
fun BookList(
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit
){
    val bookListVm: PriceVM = hiltViewModel()
    val bookPrices = bookListVm.priceList.value

    val bookNames: List<String> = bookPrices.map { it.bookName }

    BackHandler() { onBackClick() }
    Scaffold(
        topBar ={
            TopBarBookList(
                onBackClick = {onBackClick() },
                priceListTitle = "Book List"
            )
        },
        content = {
            val i = it
            BookListContent(
                bookName = bookNames,
                onBookClick = {bookName ->
                    onBookClick(bookName)
                })
        }
    )
}


@Composable
fun TopBarBookList(
    onBackClick: ()-> Unit,
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
                .weight(8f)
                .fillMaxWidth(),
            text = priceListTitle,
            style = h1,
            textAlign = TextAlign.Center,
            color = DarkBlue
        )

    }
}



@Composable
fun BookListContent(
    bookName: List<String>,
    onBookClick: (String)-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val orderVM: OrderVm = hiltViewModel()
        val orders = orderVM.order.value
        val bookProducedVm: BookRecordVM = hiltViewModel()
        val books = bookProducedVm.books.value


        LazyColumn(modifier = Modifier.fillMaxWidth() ){
            items(items = bookName) {bookName->
                val us = books.toList().filter { it.bookName == bookName }
                val totalBook = us.map { it.quantityAdd }.sumOf { it }
                val or = orders.map { it.orderDetails }.flatMap { it }
                val orderDetails = or.toList().filter { it.bookName == bookName }
                val soldBooks = orderDetails.map { it.quantity }.sumOf { it }
                val availableBook = totalBook - soldBooks
                BookItem(
                    bookName = bookName,
                    onBookClick = {onBookClick(bookName)},
                    avaliableBook = availableBook.toString()
                )
            }

        }
    }
}

@Composable
fun BookItem(
    bookName: String,
    avaliableBook: String,
    onBookClick: ()-> Unit
) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onBookClick() },
            shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
            elevation = 5.dp,
            backgroundColor = DarkBlue
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxSize()
                        .weight(8f),
                    text = bookName,
                    style = h2,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.fillMaxSize()
                        .weight(2f),
                    text = avaliableBook,
                    style = h2,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }



}


