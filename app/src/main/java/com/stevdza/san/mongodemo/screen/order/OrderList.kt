package com.stevdza.san.mongodemo.screen.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.data.order.Order
import com.stevdza.san.mongodemo.screen.homeTest.OrderItemsUI
import com.stevdza.san.mongodemo.screen.homeTest.OrderVm
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.button


@Composable
fun OrderList(
    onBackClick: ()-> Unit,
    onOrderClick: (String)-> Unit
){

    val orderVm: OrderVm = hiltViewModel()
    val orders by orderVm.order

    BackHandler() { onBackClick() }

    Scaffold(
        topBar = {
            TopBarOrderList(
                customerName = orderVm.schoolName.value,
                onCustomerNameChange = orderVm::updateSchoolName,
                onBackClick = { onBackClick() },
                onSearchClick = { orderVm.filterOrderWithSchoolName()}
            )
        },
        content = {
            ContentOrderList(
                paddingValues = it,
                onBalanceClick = { orderVm.getAllBalance() },
                onPaidClick = { orderVm.getAllPaid() },
                order = orders.sortedByDescending { it._id },
                onGetEmptyList = { orderVm.getAllOrder() },
                onOrderClick = {onOrderClick(it)}
            )
        })



}

@Composable
fun TopBarOrderList(
    customerName: String,
    onCustomerNameChange: (String)-> Unit,
    onBackClick: ()-> Unit,
    onSearchClick: ()-> Unit,

    ){
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            modifier = Modifier
                .weight(2f)
                .clickable { onBackClick() },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "ArrowBack",
            tint = DarkBlue
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
                .height(50.dp)
                .padding(horizontal = 5.dp),
            value = customerName,
            onValueChange = onCustomerNameChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            textStyle = button,
//            label = { Text(text = "* Quantity")},
            placeholder = { Text(text = "Enter Customer Name here")},
            trailingIcon = {
                Icon(modifier = Modifier.clickable { onSearchClick() }, imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions{ onSearchClick() }
        )

    }
}



@Composable
private fun ContentOrderList(
    paddingValues: PaddingValues,
    onBalanceClick: ()-> Unit,
    onPaidClick: ()-> Unit,
    order: List<Order>,
    onGetEmptyList: ()-> Unit,
    onOrderClick: (String)-> Unit


    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SectionOne(
            modifier = Modifier
                .weight(9f)
                .padding(10.dp),
            order = order,
            onGetEmptyList = { onGetEmptyList() },
            onOrderClick = { onOrderClick(it) }
        )
        SectionTwo(
            modifier = Modifier.weight(1f),
            onPaidClick = { onPaidClick() },
            onBalanceClick = {onBalanceClick()}

        )
    }
}


@Composable
private fun SectionOne(
    modifier: Modifier = Modifier,
    order: List<Order>,
    onGetEmptyList: ()-> Unit,
    onOrderClick: (String)-> Unit,

){
    Column(
        modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (order.isNotEmpty()){
            LazyColumn() {
                items(items = order ) {
                    OrderItemsUI(
                        order = it,
                        orderClick = { onOrderClick(it._id.toHexString()) }
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


@Composable
private fun SectionTwo(
    modifier: Modifier = Modifier,
    onPaidClick: ()-> Unit,
    onBalanceClick: ()-> Unit,
){
    Row(
       modifier.fillMaxSize(). padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Card(
            modifier = Modifier
                .weight(5f)
                .clickable { onBalanceClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = 5.dp,
            backgroundColor = DarkBlue
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Balance",
                    style = button,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

        }
        Spacer(modifier = Modifier.padding(20.dp))
        Card(
            modifier = Modifier
                .weight(5f)
                .clickable { onPaidClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = 5.dp,
            backgroundColor = DarkBlue
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Paid",
                    style = button,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}