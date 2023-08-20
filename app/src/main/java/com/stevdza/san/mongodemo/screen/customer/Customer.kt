package com.stevdza.san.mongodemo.screen.customer


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
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
import com.stevdza.san.mongodemo.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.stevdza.san.mongodemo.data.customer.Customer
import com.stevdza.san.mongodemo.screen.order.SelectPrice
import com.stevdza.san.mongodemo.screen.price.TopBarPriceCustomer
import com.stevdza.san.mongodemo.ui.Constants.KANO_SCHOOL
import com.stevdza.san.mongodemo.ui.Constants.REP
import com.stevdza.san.mongodemo.ui.Constants.SCHOOL_OUTSIDE_KANO
import com.stevdza.san.mongodemo.ui.Constants.SECONDARY_REP
import com.stevdza.san.mongodemo.ui.theme.DarkBlue
import com.stevdza.san.mongodemo.ui.theme.White
import com.stevdza.san.mongodemo.ui.theme.body1
import com.stevdza.san.mongodemo.ui.theme.button
import com.stevdza.san.mongodemo.ui.theme.h2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Customer(onBackClick: ()-> Unit, onPlaceOrderClick: (String)-> Unit){

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val context = LocalContext.current
    val customerVM: CustomerVM = hiltViewModel()
    val data = customerVM.customers.value
    val customer = remember { mutableStateOf<Customer?>(null) }

    BackHandler() { onBackClick() }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000L)
            customerVM.getAllCustomer()
            isRefreshing = false
        }
    }

    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val openDialog = remember { mutableStateOf(false) }
    var cus by remember { mutableStateOf("") }

    LaunchedEffect(key1 = snackbarVisible){
        if (snackbarVisible){
            delay(2000)
            snackbarVisible = false
        }
    }

    var customerType by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
                 TopBarPriceCustomer(
                     onBackClick = { onBackClick() },
                     onAddClick = {
                         customerVM.objectId.value = ""
                         customerVM.updateSchoolName("")
                         customerVM.updateSchoolAddress("")
                         customerVM.updateSchoolPhoneNumber("")
                         customerVM.updateSchoolRepName("")
                         customerVM.updateRepPhoneNumber("")
                         customerType = ""
                         scope.launch {
                             if (sheetState.isCollapsed) {
                                 sheetState.expand()
                             } else {
                                 sheetState.collapse()
                             }
                         }
                     },
                     priceListTitle = "Customer List"
                 )

        },
        content = {
            var schoolNameUp by remember { mutableStateOf("") }
            var schoolAddressUp by remember { mutableStateOf("") }
            var schoolPhoneNumberUp by remember { mutableStateOf("") }
            var schoolRepNameUp by remember { mutableStateOf("") }
            var repPhoneNumberUp by remember { mutableStateOf("") }
            customerVM.schoolNameUp.value = schoolNameUp
            customerVM.schoolAddressUp.value = schoolAddressUp
            customerVM.schoolPhoneNumberUp.value = schoolPhoneNumberUp
            customerVM.schoolRepNameUp.value = schoolRepNameUp
            customerVM.repPhoneNumberUp.value = repPhoneNumberUp
            customerVM.customerType.value = customerType

            val t = it
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    if (customerVM.objectId.value == ""){
                        CustomerForm(
                            repPhone = customerVM.repPhoneNumber.value,
                            schoolName = customerVM.schoolName.value,
                            schoolPhoneNumber = customerVM.schoolPhoneNumber.value,
                            address = customerVM.schoolAddress.value,
                            repName = customerVM.schoolRepName.value,
                            onRepPhoneChange = customerVM::updateRepPhoneNumber,
                            onSchoolNameChange = customerVM::updateSchoolName,
                            onSchoolPhoneNumber = customerVM::updateSchoolPhoneNumber,
                            onAddressChange = customerVM::updateSchoolAddress,
                            onRepNameChange = customerVM::updateSchoolRepName,
                            onAddClick = {
                                if (customerVM.schoolName.value.isNotEmpty()
                                    && customerVM.schoolPhoneNumber.value.isNotEmpty() &&
                                    customerVM.schoolAddress.value.isNotEmpty()
                                ) {
                                    val matchingSchoolName = data.find { it.schoolName == customerVM.schoolName.value }
                                    val matchingSchoolPhone = data.find { it.schoolPhoneNumber == customerVM.schoolPhoneNumber.value }
                                    val matchingRepPhone = data.find { it.schoolPhoneNumber == customerVM.repPhoneNumber.value }
                                    if ( matchingSchoolName == null && matchingSchoolPhone == null && matchingRepPhone == null){
                                        customerVM.insertCustomer()
                                        customerVM.customerType.value = customerType
                                        scope.launch {
                                            sheetState.collapse()
                                        }

                                    }else{
                                        snackbarVisible = true
                                        snackbarMessage = "School Already Exist"
                                    }

                                } else {
                                    snackbarVisible = true
                                    snackbarMessage = "some fields are empty"
                                }


                            },
                            addOrUpdateCustomer = "Add Customer",
                            customerType = customerType,
                            onKanoSchoolClick = { customerType= KANO_SCHOOL },
                            onOutSideClick = {customerType= SCHOOL_OUTSIDE_KANO},
                            onRepClick = {customerType= REP},
                            onRepSecondaryClick ={customerType= SECONDARY_REP}
                        )
                    }else{
                        CustomerForm(
                            repPhone = repPhoneNumberUp,
                            schoolName = schoolNameUp,
                            schoolPhoneNumber = schoolPhoneNumberUp,
                            address = schoolAddressUp,
                            repName = schoolRepNameUp,
                            onRepPhoneChange = {
                                repPhoneNumberUp = it
                            },
                            onSchoolNameChange = {
                                schoolNameUp= it
                            },
                            onSchoolPhoneNumber = {
                                schoolPhoneNumberUp = it
                            },
                            onAddressChange = {
                                schoolAddressUp = it
                            },
                            onRepNameChange ={
                                schoolRepNameUp = it
                            },
                            onAddClick = {
                                if (customerVM.schoolNameUp.value.isNotEmpty()
                                    && customerVM.schoolPhoneNumberUp.value.isNotEmpty() &&
                                    customerVM.schoolAddressUp.value.isNotEmpty() && customerVM.objectId.value.isNotEmpty()
                                ) {
                                    customerVM.updateCustomer()
                                    scope.launch {
                                        sheetState.collapse()
                                    }
                                } else {
                                    snackbarVisible = true
                                    snackbarMessage = "some fields are empty"
                                }
                            },
                            addOrUpdateCustomer = "Update Customer",
                            customerType = customerType,
                            onKanoSchoolClick = { customerType= KANO_SCHOOL },
                            onOutSideClick = {customerType= SCHOOL_OUTSIDE_KANO},
                            onRepClick = {customerType= REP},
                            onRepSecondaryClick ={customerType= SECONDARY_REP}
                        )
                    }
                },
                sheetPeekHeight = 0.dp
            ) {

                var hide by remember { mutableStateOf(false) }
                var hideUpade by remember { mutableStateOf("") }
                LaunchedEffect(key1 = hide ){
                        customerVM.updateCustomer()

                }

                CustomerScreen(
                    schoolName = customerVM.schoolNameSearch.value,
                    onSchoolNameChange = customerVM::updateSchoolNameSearch,
                    onSearchClick = { customerVM.filterWithSchoolName() },
                    onRefreshSlide = {isRefreshing = true},
                    onGetEmptyList = customerVM::getAllCustomer,
                    customer = data.sortedByDescending { it._id },
                    swipeRefreshState = swipeRefreshState,
                    onDeleteClick = {
                        customerVM.objectId.value = it._id.toHexString()
                        cus = it.schoolName
                        openDialog.value = true

                    },
                    onEditClick = {
                            customer ->
                        customerVM.objectId.value = customer._id.toHexString()
                        schoolNameUp = customer.schoolName
                        schoolAddressUp = customer.address
                        schoolPhoneNumberUp = customer.schoolPhoneNumber
                        schoolRepNameUp = customer.schoolRepName
                        repPhoneNumberUp = customer.repPhoneNumber
                        customerType = customer.customerType

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
                    snackbarMessage = snackbarMessage,
                    openDialog = openDialog,
                    cus = cus,
                    onYesDeleteClick = {
                        customerVM.deleteCustomer()
                    },
                    onCustomerClick = {
                            customer ->
                        customerVM.objectId.value = customer._id.toHexString()
                        schoolNameUp = customer.schoolName
                        schoolAddressUp = customer.address
                        schoolPhoneNumberUp = customer.schoolPhoneNumber
                        schoolRepNameUp = customer.schoolRepName
                        repPhoneNumberUp = customer.repPhoneNumber
                        customerType = customer.customerType
                        hide = !hide
                        customerVM.hide.value = hide
                        hideUpade = "update"
                    },
                    onPlaceOrderClick = {onPlaceOrderClick(it)},
                    onKanoSchoolCLick = {customerVM.getAllKanoCustomer()},
                    onKanoOutsideSchoolCLick = {customerVM.getAllOutSideKanoCustomer()},
                    onRepCLick = {customerVM.getAllRepCustomer()},
                    onSecondaryRepCLick ={customerVM.getAllSecondaryRepCustomer()}
                )
            }

        }
    )
}




@Composable
fun CustomerScreen(
    schoolName: String,
    onSchoolNameChange: (String)-> Unit,
    onSearchClick: ()-> Unit,
    onRefreshSlide: () -> Unit,
    onGetEmptyList: () -> Unit,
    customer: List<Customer>,
    swipeRefreshState: SwipeRefreshState,
    onEditClick: (Customer)-> Unit,
    onDeleteClick: (Customer)-> Unit,
    snackbarVisible: Boolean,
    onActionCLick: ()-> Unit,
    snackbarMessage: String,
    openDialog: MutableState<Boolean>,
    cus: String,
    onYesDeleteClick: ()-> Unit,
    onCustomerClick: (Customer)-> Unit,
    onPlaceOrderClick:(String)-> Unit,
    onKanoSchoolCLick: ()-> Unit,
    onKanoOutsideSchoolCLick: ()-> Unit,
    onRepCLick: ()-> Unit,
    onSecondaryRepCLick: ()-> Unit,
    ){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
                        Text(text = "Are you sure you want to Delete $cus ",
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

        SearchBar(
            schoolName = schoolName,
            onSchoolNameChange = onSchoolNameChange,
            onSearchClick = onSearchClick,
            onKanoSchoolCLick = onKanoSchoolCLick,
            onKanoOutsideSchoolCLick = onKanoOutsideSchoolCLick,
            onRepCLick = onRepCLick,
            onSecondaryRepCLick = onSecondaryRepCLick,
        )
        ContentScreen(
            onRefreshSlide = onRefreshSlide,
            onGetEmptyList = onGetEmptyList,
            customer = customer,
            swipeRefreshState = swipeRefreshState,
            onEditClick = { onEditClick(it) },
            onDeleteClick = { onDeleteClick(it) },
            onCustomerClick = {onCustomerClick(it)},
            onPlaceOrderClick = {onPlaceOrderClick(it)},

        )
    }

}





@Composable
fun SearchBar(
    schoolName: String,
    onSchoolNameChange: (String)-> Unit,
    onSearchClick: ()-> Unit,
    onKanoSchoolCLick: ()-> Unit,
    onKanoOutsideSchoolCLick: ()-> Unit,
    onRepCLick: ()-> Unit,
    onSecondaryRepCLick: ()-> Unit,

){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            value = schoolName,
            onValueChange = onSchoolNameChange,
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = DarkBlue,
                backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            textStyle = button,
            label = { Text(text = "Search")},
            placeholder = { Text(text = "Enter your School Name")},
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        onSearchClick()
                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = "search") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.padding(5.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp).horizontalScroll(state = rememberScrollState())) {
            Card(
                modifier = Modifier.clickable { onKanoSchoolCLick() },
                shape = RoundedCornerShape(5.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
            ) {
                Text(modifier = Modifier.padding(10.dp), text = KANO_SCHOOL, style = button)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Card(
                modifier = Modifier.clickable { onKanoOutsideSchoolCLick() },
                shape = RoundedCornerShape(5.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
            ) {
                Text(modifier = Modifier.padding(10.dp), text = SCHOOL_OUTSIDE_KANO, style = button)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Card(
                modifier = Modifier.clickable { onRepCLick() },
                shape = RoundedCornerShape(5.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
            ) {
                Text(modifier = Modifier.padding(10.dp), text = REP, style = button)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Card(
                modifier = Modifier.clickable { onSecondaryRepCLick() },
                shape = RoundedCornerShape(5.dp),
                backgroundColor = DarkBlue,
                contentColor = Color.White
            ) {
                Text(modifier = Modifier.padding(10.dp), text = SECONDARY_REP, style = button)
            }

        }

    }

}


@Composable
fun ContentScreen(
    onRefreshSlide: () -> Unit,
    onGetEmptyList: () -> Unit,
    customer: List<Customer>,
    swipeRefreshState: SwipeRefreshState,
    onDeleteClick: (Customer)-> Unit,
    onEditClick: (Customer)-> Unit,
//    hide: Boolean,
    onCustomerClick: (Customer)-> Unit,
    onPlaceOrderClick: (String)-> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onRefreshSlide()},
        ) {

            if (customer.isNotEmpty()){
                LazyColumn() {
                    items(items = customer, key = { it._id.toHexString() }) { customer ->
//                        val ide = isHide[it] == true
                        OneCustomer(
                            customer = customer,
                            onCustomerClick = {onCustomerClick(customer)},
                            onEditClick = { onDeleteClick(customer) },
                            onDeleteClick = { onEditClick(customer) },
                            placeOrderClick = {onPlaceOrderClick(customer.schoolName)}
//                            hide = hide
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
fun OneCustomer(
    customer: Customer,
    onCustomerClick: ()-> Unit,
    onEditClick: ()-> Unit,
    onDeleteClick: ()-> Unit,
    placeOrderClick: ()-> Unit
//    hide: Boolean
){

    val rotationState by animateFloatAsState(
        targetValue = if (customer.hide) 180f else 0f
    )
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Column() {

            // School Name
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCustomerClick()
//                hide = !hide
                }
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
            ) {

                Card(modifier = Modifier
//            .fillMaxSize()
                    .weight(2f),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 10.dp) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "profile_image",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Column(
                    modifier = Modifier
                        .weight(7f),
//                .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text =customer.schoolName, style = h2.copy(fontWeight = FontWeight.Bold), color = DarkBlue)
//            Text(text = "3 Ajalenkoko Magundu Street, Ikanike, Lagos", style = button)
//            Text(text = "08030857693", style = button)
                }

                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {onCustomerClick()}
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable { placeOrderClick() },
                shape = RoundedCornerShape(10.dp),
                elevation = 5.dp,
                backgroundColor = DarkBlue
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Place an Order", style = button, color = Color.White)
                }
            }

            if (customer.hide){
                // Address
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue),
//            .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
//                .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Home",
                            tint = Color.White
                        )
//                Text(text ="Pure Knowledge International Academy", style = h2.copy(fontWeight = FontWeight.Bold), color = DarkBlue)
//            Text(text = "08030857693", style = button)
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(9f)
                        .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = customer.address, style = button)


                        }
                    }

                }

                Spacer(modifier = Modifier.padding(5.dp))

                // Person Name
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue),
//            .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
//                .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Person",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(9f)
                        .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = customer.schoolRepName, style = button)


                        }
                    }

                }

                Spacer(modifier = Modifier.padding(5.dp))

                //  Phone
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue),
//            .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
//                .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(9f)
                        .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(5.dp), text = customer.customerType, style = button)
                        }
                    }

                }


                //  Phone
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue),
//            .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
//                .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "Phone",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(9f)
                        .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(5.dp), text = customer.schoolPhoneNumber, style = button)
                            if (customer.repPhoneNumber.isNotEmpty()){
                                Text(modifier = Modifier.padding(5.dp), text = customer.repPhoneNumber, style = button)
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.padding(5.dp))

                // Button
                Row(modifier = Modifier
                    .fillMaxWidth(),
//            .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(5f)
                        .clickable { onEditClick() }
                        .padding(10.dp),
                        backgroundColor = Color.Red,
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(5.dp), text = "DELETE", style = button, color = Color.White)
                        }

                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(modifier = Modifier
//            .fillMaxSize()
                        .weight(5f)
                        .clickable { onDeleteClick() }
                        .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(modifier = Modifier.padding(5.dp), text = "EDIT", style = button)
                        }
                    }
                }

            }

        }
    }





}






@Composable
fun CustomerForm(
    repPhone: String,
    schoolName: String,
    schoolPhoneNumber: String,
    address: String,
    repName: String,
    onRepPhoneChange: (String)-> Unit,
    onSchoolNameChange: (String)-> Unit,
    onSchoolPhoneNumber: (String)-> Unit,
    onAddressChange: (String)-> Unit,
    onRepNameChange: (String)-> Unit,
    onAddClick: ()-> Unit,
    addOrUpdateCustomer: String,
    customerType: String,
    onKanoSchoolClick: ()-> Unit,
    onOutSideClick: ()-> Unit,
    onRepClick: ()-> Unit,
    onRepSecondaryClick: ()-> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (customerType == ""){
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(
                onSelectPriceClick = { onKanoSchoolClick()
                },
                price = KANO_SCHOOL,
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(
                onSelectPriceClick = { onOutSideClick()
                },
                price = SCHOOL_OUTSIDE_KANO,
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(
                onSelectPriceClick = { onRepClick()
                },
                price = REP,
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SelectPrice(
                onSelectPriceClick = { onRepSecondaryClick()
                },
                price = SECONDARY_REP,
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }else{

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp),
                value = schoolName,
                onValueChange = onSchoolNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
                label = { Text(text = "* School Name") },
                placeholder = { Text(text = "Enter School Name here") },
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Menu, contentDescription = "emailIcon") },
            )

            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp),
                value = schoolPhoneNumber,
                onValueChange = onSchoolPhoneNumber,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
                label = { Text(text = "* School Phone NUmber") },
                placeholder = { Text(text = "Enter School Phone NUmber here") },
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
                value = address,
                onValueChange = onAddressChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                textStyle = button,
                label = { Text(text = "* Address") },
                placeholder = { Text(text = "Enter address here") },
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp),
                value = repName,
                onValueChange = onRepNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
                label = { Text(text = "Rep. Name") },
                placeholder = { Text(text = "Enter Rep. Name here") },
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp),
                value = repPhone,
                onValueChange = onRepPhoneChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                singleLine = true,
                textStyle = button,
                label = { Text(text = "Rep. Phone Number") },
                placeholder = { Text(text = "Enter Rep. Phone Number here") },
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
//            },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 30.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = { onAddClick() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DarkBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = addOrUpdateCustomer, style = button,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }


    }


}










