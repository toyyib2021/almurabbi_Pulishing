package com.stevdza.san.mongodemo.data.priceList

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Price : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var bookName: String =""
    var price: String =""
    var discountPrice: String =""
    var repPrice: String =""
    var repPriceDiscount: String =""
    var priceDiscountAlt: String =""

}
