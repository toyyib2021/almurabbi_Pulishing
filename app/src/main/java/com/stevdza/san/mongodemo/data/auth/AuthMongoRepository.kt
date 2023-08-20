package com.stevdza.san.mongodemo.data.auth

import com.stevdza.san.mongodemo.data.order.Order
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface AuthMongoRepository {
    fun getData(): Flow<List<Auth>>
    suspend fun getOrderWithID(_id: ObjectId): Auth
    suspend fun insertAuth(auth: Auth)
    suspend fun updateAuth(auth: Auth)
    suspend fun deleteAuth(id: ObjectId)
}


