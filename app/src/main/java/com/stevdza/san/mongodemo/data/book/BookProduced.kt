package com.stevdza.san.mongodemo.data.book

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class BookProduced : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var bookName: String = ""
    var quantityAdd: Int = 0
    var date: String = ""
    var timestamp: RealmInstant = RealmInstant.now()
}
