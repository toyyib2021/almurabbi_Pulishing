package com.stevdza.san.mongodemo.data.order

import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface OrderMongoRepository {
    fun getOrder(): Flow<List<Order>>
    fun filterOrderWithSchoolName(schoolName: String): Flow<List<Order>>
    suspend fun getOrderWithID(_id: ObjectId): Order
    suspend fun insertOrder(order: Order)
    suspend fun updateOrder(order: Order)

}


