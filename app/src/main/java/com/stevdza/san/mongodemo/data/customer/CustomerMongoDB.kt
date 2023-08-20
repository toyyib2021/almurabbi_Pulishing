package com.stevdza.san.mongodemo.data.customer

import android.util.Log
import com.stevdza.san.mongodemo.data.order.OrderMongoRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class CustomerMongoRepositoryImpl(val realm: Realm) : CustomerMongoRepository {

    override fun getData(): Flow<List<Customer>> {
        return realm.query<Customer>().asFlow().map { it.list }
    }

    override fun filterWithSchoolName(schoolName: String): Flow<List<Customer>> {
        return realm.query<Customer>(query = "schoolName CONTAINS[c] $0", schoolName)
            .asFlow().map { it.list }
    }

     override fun getCustomer(schoolName: String): Customer? {
        val cus = realm.query<Customer>(query = "schoolName CONTAINS[c] $0", schoolName).first().find()
         return cus
    }

//    override fun filterWithSchoolPhoneNumber(schoolPhoneNumber: String): Flow<List<Customer>> {
//        return realm.query<Customer>(query = "schoolPhoneNumber CONTAINS[c] $0", schoolPhoneNumber)
//            .asFlow().map { it.list }
//    }

    override suspend fun insertCustomer(customer: Customer) {
        realm.write { copyToRealm(customer) }
    }

    override suspend fun updateCustomer(customer: Customer) {
        realm.write {
            val queriedCustomer =
                query<Customer>(query = "_id == $0", customer._id)
                    .first()
                    .find()
            if (queriedCustomer != null) {
                queriedCustomer.schoolName = customer.schoolName
                queriedCustomer.schoolRepName = customer.schoolRepName
                queriedCustomer.address = customer.address
                queriedCustomer.schoolPhoneNumber = customer.schoolPhoneNumber
                queriedCustomer.repPhoneNumber = customer.repPhoneNumber
                queriedCustomer.image = customer.image
                queriedCustomer.hide = customer.hide
                queriedCustomer.customerType = customer.customerType

            } else {
                Log.d("MongoRepository", "Queried Person does not exist.")
            }
        }
    }

    override suspend fun deleteCustomer(id: ObjectId) {
        realm.write {
            try {
                val customer = query<Customer>(query = "_id == $0", id)
                    .first()
                    .find()
                customer?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepository", "${e.message}")
            }
        }
    }


}