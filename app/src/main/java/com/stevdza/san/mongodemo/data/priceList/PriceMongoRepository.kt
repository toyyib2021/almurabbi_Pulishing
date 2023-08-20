package com.stevdza.san.mongodemo.data.priceList

import com.stevdza.san.mongodemo.data.priceList.Price
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface PriceMongoRepository {
    fun getPriceList(): Flow<List<Price>>
    fun filterWithBookName(bookName: String): Flow<List<Price>>
    suspend fun insertPrice(price: Price)
    suspend fun updatePrice(price: Price)
    suspend fun deletePrice(id: ObjectId)
}


