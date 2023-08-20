package com.stevdza.san.mongodemo.data.order

import com.stevdza.san.mongodemo.model.Person
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class OrderRepositoryImpl(val realm: Realm) : OrderMongoRepository{

    override fun getOrder(): Flow<List<Order>> {
        return realm.query<Order>().asFlow().map { it.list }
    }

    override fun filterOrderWithSchoolName(schoolName: String): Flow<List<Order>> {
        return realm.query<Order>(query = "schoolName CONTAINS[c] $0", schoolName).asFlow().map { it.list }
    }

    override suspend fun getOrderWithID(_id: ObjectId): Order {
        return realm.query<Order>(query = "_id == $0", _id).first().find() as Order
    }

    override suspend fun insertOrder(order: Order) {
        realm.write { copyToRealm(order) }
    }

    override suspend fun updateOrder(order: Order) {
        realm.write {
            val queriedPerson = query<Order>(query = "_id == $0", order._id).first().find()
            queriedPerson?.schoolName= order.schoolName
            queriedPerson?.schoolPhone= order.schoolPhone
            queriedPerson?.paid= order.paid
            queriedPerson?.date= order.date
            queriedPerson?.priceType= order.priceType
            queriedPerson?.totalAmount= order.totalAmount
            queriedPerson?.orderDetails= order.orderDetails
            queriedPerson?.payment= order.payment

        }
    }

}