package com.stevdza.san.mongodemo.screen.price


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.data.priceList.PriceMongoRepository
import com.stevdza.san.mongodemo.data.priceList.PriceMongoRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import javax.inject.Inject

@HiltViewModel
class PriceVM @Inject constructor(
    private val repository: PriceMongoRepository
) : ViewModel() {
    var objectId = mutableStateOf("")
    var bookName = mutableStateOf("")
    var price = mutableStateOf("")
    var discountPrice = mutableStateOf("")
    var discountPriceTwo = mutableStateOf("")
    var repPrice = mutableStateOf("")
    var repPriceDiscount = mutableStateOf("")

    var addOrUpdate = mutableStateOf("")
    var addAndUpdate = mutableStateOf("")



    var priceList = mutableStateOf(emptyList<Price>())



    init {

        getPriceList()

    }





     fun getPriceList() {
        viewModelScope.launch {
            repository.getPriceList().collect {
                priceList.value = it
            }
        }
    }

    fun addAndUpdate(add: String){
        addAndUpdate.value = add
    }

    fun updateBookName(bookName: String) {
        this.bookName.value = bookName
    }

    fun updatePrice(price: String) {
        this.price.value = price
    }

    fun updateDiscountPrice(discountPrice: String) {
        this.discountPrice.value = discountPrice
    }

    fun updateRepPrice(repPrice: String) {
        this.repPrice.value = repPrice
    }

    fun updateRepPriceDiscount(repPriceDiscount: String) {
        this.repPriceDiscount.value = repPriceDiscount
    }

    fun updatePriceDiscountTwo(discountPriceTwo: String) {
        this.discountPriceTwo.value = discountPriceTwo
    }


    fun insertPrice() {
        viewModelScope.launch(Dispatchers.IO) {
            if (
                bookName.value.isNotEmpty() && repPrice.value.isNotEmpty() &&
                        discountPrice.value.isNotEmpty() && repPrice.value.isNotEmpty()
            ) {
                repository.insertPrice(price = Price().apply {
                     bookName = this@PriceVM.bookName.value
                    price =this@PriceVM.price.value
                    discountPrice =this@PriceVM.discountPrice.value
                    repPrice =this@PriceVM.repPrice.value
                    repPriceDiscount =this@PriceVM.repPriceDiscount.value
                    priceDiscountAlt =this@PriceVM.discountPriceTwo.value
                })
            }
        }
    }

    fun updatePriceList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()){
                repository.updatePrice(price = Price().apply {
                    _id = BsonObjectId(hexString = this@PriceVM.objectId.value)
                    bookName = this@PriceVM.bookName.value
                    price =this@PriceVM.price.value
                    discountPrice =this@PriceVM.discountPrice.value
                    repPrice =this@PriceVM.repPrice.value
                    repPriceDiscount =this@PriceVM.repPriceDiscount.value
                    priceDiscountAlt =this@PriceVM.discountPriceTwo.value
                })
            }

        }
    }

    fun deleteBook() {
        viewModelScope.launch {
            if (objectId.value.isNotEmpty()) {
                repository.deletePrice(id = BsonObjectId(hexString = objectId.value))
            }
        }
    }

    fun filterWithBookName() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterWithBookName(bookName = bookName.value).collect{
                priceList.value = it
            }
        }
    }


}