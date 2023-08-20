package com.stevdza.san.mongodemo.data.priceList

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class PriceMongoRepositoryImp(val realm: Realm): PriceMongoRepository{

    override fun getPriceList(): Flow<List<Price>> {
        return realm.query<Price>().asFlow().map { it.list }
    }

    override fun filterWithBookName(bookName: String): Flow<List<Price>> {
        return realm.query<Price>(query = "schoolName CONTAINS[c] $0", bookName)
            .asFlow().map { it.list }
    }

    override suspend fun insertPrice(price: Price) {
        realm.write { copyToRealm(price) }
    }


    override suspend fun updatePrice(price: Price) {
        realm.write {
            val queriedPrice =
                query<Price>(query = "_id == $0", price._id)
                    .first()
                    .find()
            if (queriedPrice != null) {
                queriedPrice.bookName = price.bookName
                queriedPrice.price = price.price
                queriedPrice.discountPrice = price.discountPrice
                queriedPrice.repPrice = price.repPrice
                queriedPrice.repPriceDiscount = price.repPriceDiscount
                queriedPrice.priceDiscountAlt = price.priceDiscountAlt
            } else {
                Log.d("MongoRepository", "Queried Person does not exist.")
            }
        }
    }

    override suspend fun deletePrice(id: ObjectId) {
        realm.write {
            try {
                val price = query<Price>(query = "_id == $0", id)
                    .first()
                    .find()
                price?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepository", "${e.message}")
            }
        }
    }


}