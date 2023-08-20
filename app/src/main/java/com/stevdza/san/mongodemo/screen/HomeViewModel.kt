package com.stevdza.san.mongodemo.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.auth.Auth
import com.stevdza.san.mongodemo.data.auth.AuthMongoRepository
import com.stevdza.san.mongodemo.data.book.BookProduced
import com.stevdza.san.mongodemo.data.order.Order
import com.stevdza.san.mongodemo.data.priceList.Price
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import javax.inject.Inject

@HiltViewModel
class AuthVm @Inject constructor(
    private val repository: AuthMongoRepository
) : ViewModel() {

    var objectId = mutableStateOf("")
    var accessKey = mutableStateOf("")
    var sellerKey = mutableStateOf("")

    var data = mutableStateOf(emptyList<Auth>())
    var authById = mutableStateOf<Auth?>(null)


    init {
        viewModelScope.launch {
            repository.getData().collect {
                data.value = it
            }
        }
    }

    fun getOrderListById() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrderWithID(_id = BsonObjectId(hexString = this@AuthVm.objectId.value)).let {
                authById.value = it
            }
        }

    }

    fun insertAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            if (sellerKey.value.isNotEmpty() && accessKey.value.isNotEmpty()) {
                repository.insertAuth(auth = Auth().apply {
                    accessKey = this@AuthVm.accessKey.value
                    sellerKey = this@AuthVm.sellerKey.value
                })

            }
        }
    }

    fun updateAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()){
                repository.updateAuth(auth = Auth().apply {
                    _id = BsonObjectId(hexString = this@AuthVm.objectId.value)
                    accessKey = this@AuthVm.accessKey.value
                    sellerKey = this@AuthVm.sellerKey.value
                })
            }

        }
    }
}