package com.stevdza.san.mongodemo.data.book

import android.util.Log
import com.stevdza.san.mongodemo.data.MongoRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class BookMongoRepositoryImp(val realm: Realm) : BookMongoRepository {

    override fun getData(): Flow<List<BookProduced>> {
        return realm.query<BookProduced>().asFlow().map { it.list }
    }


    override fun filterData(bookName: String): Flow<List<BookProduced>> {
        return realm.query<BookProduced>(query = "bookName CONTAINS[c] $0", bookName)
            .asFlow().map { it.list }
    }

    override suspend fun insertBookRecord(bookProduced: BookProduced) {
        realm.write { copyToRealm(bookProduced) }
    }

    override suspend fun updateBookRecord(bookProduced: BookProduced) {
        realm.write {
            val queriedPerson =
                query<BookProduced>(query = "_id == $0", bookProduced._id)
                    .first()
                    .find()
            if (queriedPerson != null) {
                queriedPerson.bookName = bookProduced.bookName
                queriedPerson.quantityAdd = bookProduced.quantityAdd
                queriedPerson.date = bookProduced.date

            } else {
                Log.d("MongoRepository", "Queried Person does not exist.")
            }
        }
    }

    override suspend fun deleteBookRecord(id: ObjectId) {
        realm.write {
            try {
                val bookProduced = query<BookProduced>(query = "_id == $0", id)
                    .first()
                    .find()
                bookProduced?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepository", "${e.message}")
            }
        }
    }
}