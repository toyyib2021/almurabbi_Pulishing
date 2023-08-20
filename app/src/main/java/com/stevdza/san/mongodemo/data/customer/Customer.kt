package com.stevdza.san.mongodemo.data.customer

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Customer: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var schoolName: String = ""
    var schoolRepName: String = ""
    var address: String = ""
    var schoolPhoneNumber: String =""
    var repPhoneNumber: String = ""
    var image: String = ""
    var hide: Boolean = false
    var customerType: String = ""
}