package com.stevdza.san.mongodemo.screen.homeTest

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.order.Order
import com.stevdza.san.mongodemo.data.order.OrderDetails
import com.stevdza.san.mongodemo.data.order.OrderMongoRepository
import com.stevdza.san.mongodemo.data.order.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class OrderVm @Inject constructor(
    private val repository: OrderMongoRepository
) : ViewModel() {

    var objectId = mutableStateOf("")
    var schoolName = mutableStateOf("")
    var schoolPhone = mutableStateOf("")
    var date = mutableStateOf("")
    var orderDate = mutableStateOf("")
    var paid = mutableStateOf("0")
    var totalAmount = mutableStateOf("0")
    var bookName = mutableStateOf("")
    var qty = mutableStateOf("0")
    var priceType = mutableStateOf("")
    var unitPrice = mutableStateOf("0")
    var paidList = mutableStateOf("0")
    var dateList = mutableStateOf("")
    var modifiedDate = mutableStateOf("")
    var amount = mutableIntStateOf(0)


    var orderDetailsList: RealmList<OrderDetails> = realmListOf()
    var paymentList: RealmList<Payment> = realmListOf()


    var order = mutableStateOf(emptyList<Order>())
    var orderById = mutableStateOf<Order?>(null)



    init {
        getAllOrder()
    }




    fun getAllOrder() {
        viewModelScope.launch {
            repository.getOrder().collect {
                order.value = it
            }
        }
    }

    fun updateSchoolName(schoolName: String) {
        this.schoolName.value = schoolName
    }

    fun updateSchoolPhone(schoolPhone: String) {
        this.schoolPhone.value = schoolPhone
    }

    fun updatePaid(paid: String) {
        this.paid.value = paid
    }

    fun updateAmount(amount: Int) {
        this.amount.value = amount
    }

    fun updateTotalAmount(totalAmount: String) {
        this.totalAmount.value = totalAmount
    }

    fun updateBookName(bookName: String) {
        this.bookName.value = bookName
    }
    fun updateQty(qty: String) {
        this.qty.value = qty
    }
    fun updateUnitPrice(unitPrice: String) {
        this.unitPrice.value = unitPrice
    }

    fun updatePaidList(paidList: String) {
        this.paidList.value = paidList
    }






    fun addOrderDetails() {
            val s = OrderDetails().apply {
                bookName = this@OrderVm.bookName.value
                quantity = this@OrderVm.qty.value.toInt()
                unitPrice = this@OrderVm.unitPrice.value.toInt()
                amount = this@OrderVm.qty.value.toInt() * this@OrderVm.unitPrice.value.toInt()
                date = this@OrderVm.date.value
            }
            orderDetailsList.add(s)
    }




    fun deleteOrderDetails(id: Int){
       val w = orderDetailsList[id]
        orderDetailsList.remove(w)
    }

    fun addPayment(){
            val s = Payment().apply {
                paid = this@OrderVm.paidList.value.toInt()
                date = this@OrderVm.dateList.value
                updateDate = this@OrderVm.modifiedDate.value
            }
            paymentList.add(s)
    }

    fun getPayment(id: Int): Payment{
            return paymentList[id]
    }


    fun insertOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            if (
                schoolName.value.isNotEmpty() && schoolPhone.value.isNotEmpty() &&
                date.value.isNotEmpty() && paid.value.isNotEmpty() &&
                totalAmount.value.isNotEmpty() && orderDetailsList.isNotEmpty()
            ) {
                repository.insertOrder(order = Order().apply {
                    schoolName = this@OrderVm.schoolName.value
                    schoolPhone = this@OrderVm.schoolPhone.value
                    priceType = this@OrderVm.priceType.value
                    date = this@OrderVm.orderDate.value
                    paid = this@OrderVm.paid.value.toInt()
                    totalAmount = this@OrderVm.totalAmount.value.toInt()
                    orderDetails.addAll(orderDetailsList)
                    payment.addAll(paymentList)
                })
            }
        }
    }

    fun updateOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()) {
                repository.updateOrder(order = Order().apply {
                    _id = ObjectId(hexString = this@OrderVm.objectId.value)
                    schoolName = this@OrderVm.schoolName.value
                    schoolPhone = this@OrderVm.schoolPhone.value
                    priceType = this@OrderVm.priceType.value
                    date = this@OrderVm.orderDate.value
                    paid = this@OrderVm.paid.value.toInt()
                    totalAmount = this@OrderVm.totalAmount.value.toInt()
                    orderDetails.addAll(orderDetailsList)
                    payment.addAll(paymentList)
                })
            }

        }
    }



    fun getOrderListById() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrderWithID(_id = ObjectId(hexString = this@OrderVm.objectId.value)).let {
                orderById.value = it
            }
        }

    }




    fun getAllPaid(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrder().collect {
                it.filter {
                    it.paid == it.totalAmount
                }.let {
                    order.value = it
                }
            }
        }
    }

    fun getAllBalance(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrder().collect {
                it.filter {
                    it.paid != it.totalAmount
                }.let {
                    order.value = it
                }
            }
        }
    }



    fun filterOrderWithSchoolName(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterOrderWithSchoolName(schoolName = schoolName.value).collect {
                    order.value = it
            }
        }
    }



//     fun filterOrder(schoolName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (schoolName == "") {
//                OrderMongoDB.getOrder().collect{
//                    filteredOrder.value = it
//                }
//            } else {
//                OrderMongoDB.filterOrderWithSchoolName(schoolName = schoolName).collect {
//                    filteredOrder.value = it
//                }
//            }
//        }
//    }

}