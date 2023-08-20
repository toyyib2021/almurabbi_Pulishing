package com.stevdza.san.mongodemo.data.auth

import android.util.Log
import com.stevdza.san.mongodemo.data.MongoRepository
import com.stevdza.san.mongodemo.data.order.Order
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class AuthRepositoryImp(val realm: Realm) : AuthMongoRepository {

    override fun getData(): Flow<List<Auth>> {
        return realm.query<Auth>().asFlow().map { it.list }
    }

    override suspend fun getOrderWithID(_id: ObjectId): Auth {
        return realm.query<Auth>(query = "_id == $0", _id).first().find() as Auth
    }

    override suspend fun insertAuth(auth: Auth) {
        realm.write { copyToRealm(auth) }
    }

    override suspend fun updateAuth(auth: Auth) {
        realm.write {
            val queriedPerson =
                query<Auth>(query = "_id == $0", auth._id)
                    .first()
                    .find()
            if (queriedPerson != null) {
                queriedPerson.accessKey = auth.accessKey
                queriedPerson.sellerKey = auth.sellerKey

            } else {
                Log.d("MongoRepository", "Queried Person does not exist.")
            }
        }
    }

    override suspend fun deleteAuth(id: ObjectId) {
        realm.write {
            try {
                val auth = query<Auth>(query = "_id == $0", id)
                    .first()
                    .find()
                auth?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepository", "${e.message}")
            }
        }
    }
}