package com.stevdza.san.mongodemo.screen.homeTest

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdza.san.mongodemo.R
import com.stevdza.san.mongodemo.ui.string.StringsSet.CLICK_HERE
import com.stevdza.san.mongodemo.ui.string.StringsSet.HI
import com.stevdza.san.mongodemo.ui.string.StringsSet.PLACE_AN_ORDER
import com.stevdza.san.mongodemo.ui.string.StringsSet.PRODUCT_RECORD
import com.stevdza.san.mongodemo.ui.string.StringsSet.SALES_RECORD
import com.stevdza.san.mongodemo.ui.string.StringsSet.SEE_ALL
import com.stevdza.san.mongodemo.ui.string.StringsSet.UPDATE_PRODUCTS_RECORD
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h1
import com.stevdza.san.mongodemo.ui.theme.h2
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.stevdza.san.mongodemo.MainActivity
import com.stevdza.san.mongodemo.data.order.Order
import com.stevdza.san.mongodemo.screen.AuthVm
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.LightBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(
    onUpdateProductRecordClick: ()-> Unit,
    onOrderClick: (String)-> Unit,
    onPriceListClicked: ()-> Unit,
    onCustomerListClick: ()-> Unit,
    onViewBooksRecordClick: ()-> Unit,
    onSeeAllClick: ()-> Unit,
    onHiClick: ()-> Unit,
){
    val orderVM: OrderVm = hiltViewModel()
    val order by orderVM.order
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            orderVM.getAllOrder()
            delay(2000L)
            isRefreshing = false
        }
    }

    var backPressedTime: Long = 0

    val authKeyVm: AuthVm = hiltViewModel()
    val objectId by remember { mutableStateOf("64e05169576408175f7bfcca") }
    val date = authKeyVm.authById


    val accessKeyCheck = date.value?.accessKey
    val sellerKeyCheck = date.value?.sellerKey


    authKeyVm.objectId.value = objectId

    LaunchedEffect(key1 = true ){
        authKeyVm.getOrderListById()
    }

    BackHandler() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            val activity: MainActivity = MainActivity()
            // on below line we are finishing activity.
            activity.finish()
            System.exit(0)
        } else {
            Log.i(TAG, "home: press back button again ")
        }
        backPressedTime = System.currentTimeMillis()
    }

    if (accessKeyCheck != null) {
        HomeUI(
            onPriceListClicked = {
                onPriceListClicked()

            },
            onCustomerListClick = { onCustomerListClick() },
            onViewBooksRecordClick = { onViewBooksRecordClick() },
            onSeeAllClick = { onSeeAllClick() },
            order = order.sortedByDescending{it._id},
            onRefreshSlide = { isRefreshing = true},
            swipeRefreshState = swipeRefreshState,
            onOrderClick = { onOrderClick(it)},
            onUpdateProductRecordClick = {

                onUpdateProductRecordClick()
                                         },
            onHiClick = {onHiClick()},
            accessKey = accessKeyCheck

        )
    }
}







@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeUI(
    onPriceListClicked: ()-> Unit,
    onCustomerListClick: ()-> Unit,
    onViewBooksRecordClick: () -> Unit,
    onSeeAllClick: ()-> Unit,
    order: List<Order>,
    onRefreshSlide: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    onOrderClick: (String)-> Unit,
    onUpdateProductRecordClick: ()-> Unit,
    onHiClick: ()-> Unit,
    accessKey: String

    ){

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            SectionOne(
                modifier = Modifier.weight(1f),
                onPriceListClicked = { onPriceListClicked() },
                onCustomerListClick = {  onCustomerListClick()},
                onHiClick={onHiClick()}
            )
            SectionTwo(
                modifier = Modifier.weight(3f),
                onViewBooksRecordClick = onViewBooksRecordClick
            )
            SectionThree(
                modifier = Modifier.weight(4.5f),
                onSeeAllClick = { onSeeAllClick() },
                order = order,
                onRefreshSlide = { onRefreshSlide() },
                swipeRefreshState = swipeRefreshState,
                onOrderClick = {onOrderClick(it)}
            )
                Log.e(TAG, "order: $order", )
            SectionFour(
                modifier = Modifier.weight(1.5f),
                onUpdateProductRecordClick = { onUpdateProductRecordClick() },
                accessKey = accessKey
            )
    }



}

@Composable
fun SectionOne(
    modifier: Modifier = Modifier,
    onPriceListClicked: ()-> Unit,
    onCustomerListClick: ()-> Unit,
    onHiClick: ()-> Unit,
){

    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            modifier = Modifier
                .weight(6f)
                .clickable { onHiClick() }
                .fillMaxWidth(),
            text = HI,
            style = h1,
            color = DarkGray ,
            textAlign = TextAlign.Left
        )

        Image(
            modifier = Modifier
                .weight(2f)
                .clickable { onPriceListClicked() }
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.price_list),
            contentDescription = "price_list",
            contentScale = ContentScale.Fit
        )
        Image(
            modifier = Modifier
                .weight(2f)
                .clickable { onCustomerListClick() }
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.customer_list),
            contentDescription = "customer_list",
            contentScale = ContentScale.Fit
        )

    }

}


@Composable
fun SectionTwo(
    modifier: Modifier = Modifier,
    onViewBooksRecordClick: () -> Unit
){
    Column(modifier
//        .weight(3f)
        .fillMaxWidth()
        .clickable { onViewBooksRecordClick() }
        .padding(start = 20.dp, end = 20.dp)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = PRODUCT_RECORD,
            style = button,
            textAlign = TextAlign.Left
        )
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = LightBlue,
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = """
                            Want to view
                            all Book's Record
                        """.trimIndent(),
                        textAlign = TextAlign.Right,
                        style = h2,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
//                            Text(text = "Want to view")
                        Text(text = CLICK_HERE, color = Color.Black)
                    }

                }
                Column(modifier = Modifier.weight(5f)) {

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.home_screen_image),
                        contentDescription = "home_screen_image",
                        alignment = Alignment.BottomEnd,
                        contentScale = ContentScale.Fit

                    )

                }
            }
        }
    }
}

@Composable
fun SectionThree(
    modifier: Modifier = Modifier,
    onSeeAllClick: ()-> Unit,
    order: List<Order>,
    onRefreshSlide: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    onOrderClick: (String)-> Unit

){
    Column(
        modifier
//            .weight(4.5f)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onSeeAllClick() },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = SALES_RECORD,
                style = button)

            Text(
                text = SEE_ALL,
                style = button)

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .background(DarkGray)
                .padding(horizontal = 10.dp)
        ) {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { onRefreshSlide()},
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items = order, key = { it._id.toHexString() }) {
                        OrderItemsUI(
                            order = it,
                            orderClick = {
                                onOrderClick(it._id.toHexString())
                            }
                        )
                    }


                }

            }

        }
    }
}

@Composable
fun OrderItemsUI(order: Order, orderClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 5.dp)
            .clickable { orderClick() },
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp),
        elevation = 5.dp
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(6f)
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = order.schoolName,
                        style = button,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = order.schoolPhone,
                        style = body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .height(80.dp),
            ) {
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    backgroundColor = DarkBlue,
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "₦${order.totalAmount}",
                            style = h2,
                            color = Color.White
                        )
                    }

                }
                Spacer(modifier = Modifier.padding(3.dp))
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    backgroundColor = LightBlue,
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Bal: ₦${order.totalAmount - order.paid}",
                            style = body1
                                .copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                }

            }
        }


    }
}



@Composable
fun SectionFour(
    modifier: Modifier = Modifier,
//    onPlaceOrderClick: ()-> Unit,
    onUpdateProductRecordClick: ()-> Unit,
    accessKey: String
){
    Column(
        modifier
//            .weight(1.5f)
            .padding(20.dp)
    ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                backgroundColor = White,
                border = BorderStroke(2.dp, DarkBlue)
            ) {
                Column(
                    modifier = Modifier
                        .weight(5.2f)
                        .fillMaxSize()
                        .clickable { onUpdateProductRecordClick() },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = UPDATE_PRODUCTS_RECORD,
                        style = body1,
                        color = DarkBlue
                    )

                }

            }
        }


}

