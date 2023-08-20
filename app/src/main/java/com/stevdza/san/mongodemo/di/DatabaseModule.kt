package com.stevdza.san.mongodemo.di

import com.stevdza.san.mongodemo.data.MongoRepository
import com.stevdza.san.mongodemo.data.MongoRepositoryImpl
import com.stevdza.san.mongodemo.data.auth.Auth
import com.stevdza.san.mongodemo.data.auth.AuthMongoRepository
import com.stevdza.san.mongodemo.data.auth.AuthRepositoryImp
import com.stevdza.san.mongodemo.data.book.BookMongoRepository
import com.stevdza.san.mongodemo.data.book.BookMongoRepositoryImp
import com.stevdza.san.mongodemo.data.book.BookProduced
import com.stevdza.san.mongodemo.data.customer.Customer
import com.stevdza.san.mongodemo.data.customer.CustomerMongoRepository
import com.stevdza.san.mongodemo.data.customer.CustomerMongoRepositoryImpl
import com.stevdza.san.mongodemo.data.order.Order
import com.stevdza.san.mongodemo.data.order.OrderDetails
import com.stevdza.san.mongodemo.data.order.OrderMongoRepository
import com.stevdza.san.mongodemo.data.order.OrderRepositoryImpl
import com.stevdza.san.mongodemo.data.order.Payment
import com.stevdza.san.mongodemo.data.priceList.Price
import com.stevdza.san.mongodemo.data.priceList.PriceMongoRepository
import com.stevdza.san.mongodemo.data.priceList.PriceMongoRepositoryImp
import com.stevdza.san.mongodemo.model.Address
import com.stevdza.san.mongodemo.model.Person
import com.stevdza.san.mongodemo.model.Pet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Order::class, OrderDetails::class, Payment::class, BookProduced::class, Customer::class, Price::class, Auth::class))
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideOrderRepository(realm: Realm): OrderMongoRepository {
        return OrderRepositoryImpl(realm = realm)
    }

    @Singleton
    @Provides
    fun provideBookRepository(realm: Realm): BookMongoRepository {
        return BookMongoRepositoryImp(realm = realm)
    }

    @Singleton
    @Provides
    fun provideCustomerRepository(realm: Realm): CustomerMongoRepository {
        return CustomerMongoRepositoryImpl(realm = realm)
    }


    @Singleton
    @Provides
    fun providePriceRepository(realm: Realm): PriceMongoRepository {
        return PriceMongoRepositoryImp(realm = realm)

    }

    @Singleton
    @Provides
    fun provideAuthRepository(realm: Realm): AuthMongoRepository {
        return AuthRepositoryImp(realm = realm)

    }
}