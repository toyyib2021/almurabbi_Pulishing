package com.stevdza.san.mongodemo.data.order


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class Order : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var schoolName: String = ""
    var schoolPhone: String =""
    var paid: Int = 0
    var date: String =""
    var totalAmount: Int = 0
    var priceType: String = ""
    var orderDetails: RealmList<OrderDetails> = realmListOf()
    var payment: RealmList<Payment> = realmListOf()
    var timestamp: RealmInstant = RealmInstant.now()
}


class OrderDetails: EmbeddedRealmObject {
    var bookName: String = ""
    var quantity: Int = 0
    var unitPrice: Int = 0
    var amount: Int = 0
    var date: String =""

}

class Payment: EmbeddedRealmObject {
    var paid: Int = 0
    var date: String = ""
    var updateDate: String = ""
}
