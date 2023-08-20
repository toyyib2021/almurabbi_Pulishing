package com.stevdza.san.mongodemo.data.customer

import com.stevdza.san.mongodemo.data.customer.Customer
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface CustomerMongoRepository {
    fun getData(): Flow<List<Customer>>
    fun filterWithSchoolName(schoolName: String): Flow<List<Customer>>
    fun getCustomer(schoolName: String): Customer?
//    fun filterWithSchoolPhoneNumber(schoolPhoneNumber: String): Flow<List<Customer>>
    suspend fun insertCustomer(customer: Customer)
    suspend fun updateCustomer(customer: Customer)
    suspend fun deleteCustomer(id: ObjectId)
}


