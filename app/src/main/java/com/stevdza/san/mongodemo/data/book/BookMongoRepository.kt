package com.stevdza.san.mongodemo.data.book

import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface BookMongoRepository {
    fun getData(): Flow<List<BookProduced>>
    fun filterData(bookName: String): Flow<List<BookProduced>>
    suspend fun insertBookRecord(bookProduced: BookProduced)
    suspend fun updateBookRecord(bookProduced: BookProduced)
    suspend fun deleteBookRecord(id: ObjectId)
}


