package com.stevdza.san.mongodemo.screen.customer


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.customer.Customer
import com.stevdza.san.mongodemo.data.customer.CustomerMongoRepository
import com.stevdza.san.mongodemo.ui.Constants
import com.stevdza.san.mongodemo.ui.Constants.KANO_SCHOOL
import com.stevdza.san.mongodemo.ui.Constants.REP
import com.stevdza.san.mongodemo.ui.Constants.SCHOOL_OUTSIDE_KANO
import com.stevdza.san.mongodemo.ui.Constants.SECONDARY_REP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import javax.inject.Inject


@HiltViewModel
class CustomerVM @Inject constructor(
    private val repository: CustomerMongoRepository
) : ViewModel() {
    var objectId = mutableStateOf("")
    var schoolName = mutableStateOf("")
    var schoolRepName = mutableStateOf("")
    var schoolAddress = mutableStateOf("")
    var schoolPhoneNumber = mutableStateOf("")
    var repPhoneNumber = mutableStateOf("")

    var schoolNameUp = mutableStateOf("")
    var schoolRepNameUp = mutableStateOf("")
    var schoolAddressUp = mutableStateOf("")
    var schoolPhoneNumberUp = mutableStateOf("")
    var repPhoneNumberUp = mutableStateOf("")
    var customerType = mutableStateOf("")
    var hide = mutableStateOf(false)


    var image = mutableStateOf("img")
    var schoolNameSearch = mutableStateOf("")


    var customers = mutableStateOf(emptyList<Customer>())
    var oneCustomer = mutableStateOf<Customer?>(null)


    init {
        getAllCustomer()

    }

    fun getAllCustomer() {
        viewModelScope.launch {
            repository.getData().collect {
                customers.value = it
            }
        }
    }

    fun updateSchoolNameSearch(schoolNameSearch: String) {
        this.schoolNameSearch.value = schoolNameSearch
    }

    fun updateSchoolName(schoolName: String) {
        this.schoolName.value = schoolName
    }

    fun updateSchoolRepName(schoolRepName: String) {
        this.schoolRepName.value = schoolRepName
    }

    fun updateSchoolAddress(schoolAddress: String) {
        this.schoolAddress.value = schoolAddress
    }

    fun updateSchoolPhoneNumber(schoolPhoneNumber: String) {
        this.schoolPhoneNumber.value = schoolPhoneNumber
    }

    fun updateRepPhoneNumber(repPhoneNumber: String) {
        this.repPhoneNumber.value = repPhoneNumber
    }

    fun insertCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            if (
                schoolName.value.isNotEmpty() && schoolPhoneNumber.value.isNotEmpty() &&
                        schoolAddress.value.isNotEmpty()
            ) {
                repository.insertCustomer(customer = Customer().apply {
                    schoolName =this@CustomerVM.schoolName.value
                    schoolRepName =this@CustomerVM.schoolRepName.value
                    address =this@CustomerVM.schoolAddress.value
                    schoolPhoneNumber =this@CustomerVM.schoolPhoneNumber.value
                    repPhoneNumber =this@CustomerVM.repPhoneNumber.value
                    image =this@CustomerVM.image.value
                    hide =this@CustomerVM.hide.value
                    customerType =this@CustomerVM.customerType.value
                })
            }
        }
    }

    fun updateCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()){
                repository.updateCustomer(customer = Customer().apply {
                    _id = BsonObjectId(hexString = this@CustomerVM.objectId.value)
                    schoolName = this@CustomerVM.schoolNameUp.value
                    schoolRepName =this@CustomerVM.schoolRepNameUp.value
                    address =this@CustomerVM.schoolAddressUp.value
                    schoolPhoneNumber =this@CustomerVM.schoolPhoneNumberUp.value
                    repPhoneNumber =this@CustomerVM.repPhoneNumberUp.value
                    image =this@CustomerVM.image.value
                    hide =this@CustomerVM.hide.value
                    customerType =this@CustomerVM.customerType.value

                })
            }

        }
    }

    fun deleteCustomer() {
        viewModelScope.launch {
            if (objectId.value.isNotEmpty()) {
                repository.deleteCustomer(id = BsonObjectId(hexString = objectId.value))
            }
        }
    }

    fun filterWithSchoolName() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterWithSchoolName(schoolName = schoolNameSearch.value).collect{
                customers.value = it
            }
        }
    }


    fun getCustomer(schoolName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCustomer(schoolName = schoolName).let { customer ->
                oneCustomer.value = customer
            }
        }
    }

    fun getAllKanoCustomer(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().collect {
                it.filter {
                    it.customerType == KANO_SCHOOL
                }.let {
                    customers.value = it
                }
            }
        }
    }

    fun getAllOutSideKanoCustomer(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().collect {
                it.filter {
                    it.customerType == SCHOOL_OUTSIDE_KANO
                }.let {
                    customers.value = it
                }
            }
        }
    }

    fun getAllRepCustomer(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().collect {
                it.filter {
                    it.customerType == REP
                }.let {
                    customers.value = it
                }
            }
        }
    }

    fun getAllSecondaryRepCustomer(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().collect {
                it.filter {
                    it.customerType == SECONDARY_REP
                }.let {
                    customers.value = it
                }
            }
        }
    }
}