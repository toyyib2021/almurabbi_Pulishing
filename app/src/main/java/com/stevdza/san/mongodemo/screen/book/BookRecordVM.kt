package com.stevdza.san.mongodemo.screen.book

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.book.BookMongoRepository
import com.stevdza.san.mongodemo.data.book.BookProduced
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import javax.inject.Inject

@HiltViewModel
class BookRecordVM @Inject constructor(
    private val repository: BookMongoRepository
) : ViewModel() {

    var bookName = mutableStateOf("")
    var bookQty = mutableStateOf("0")
    var date = mutableStateOf("")


    var uBookName = mutableStateOf("")
    var uBookQty = mutableStateOf("0")
    var uDate = mutableStateOf("")

//    var pet: RealmList<Pet> = realmListOf()
    var objectId = mutableStateOf("")
//    var filtered = mutableStateOf(false)
    var books = mutableStateOf(emptyList<BookProduced>())



    init {
        fechtAllBookRecord()

    }

     fun fechtAllBookRecord() {
        viewModelScope.launch {
            repository.getData().collect {
                books.value = it
            }
        }
    }

    fun updatbookQty(bookQty: String) {
        this.bookQty.value = bookQty
    }

    fun updateBookName(bookName: String) {
        this.bookName.value = bookName
    }


    fun insertBookRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            if (bookName.value.isNotEmpty()) {
                repository.insertBookRecord(bookProduced = BookProduced().apply {
                    bookName = this@BookRecordVM.bookName.value
                    quantityAdd = this@BookRecordVM.bookQty.value.toInt()
                    date = this@BookRecordVM.date.value
                })

            }
        }
    }

    fun updateBookRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()){
                repository.updateBookRecord(bookProduced = BookProduced().apply {
                    _id = BsonObjectId(hexString = this@BookRecordVM.objectId.value)
                    bookName = this@BookRecordVM.uBookName.value
                    quantityAdd = this@BookRecordVM.uBookQty.value.toInt()
                    date = this@BookRecordVM.uDate.value
                })
            }

        }
    }

    fun deletePerson() {
        viewModelScope.launch {
            if (objectId.value.isNotEmpty()) {
                repository.deleteBookRecord(id = BsonObjectId(hexString = objectId.value))
            }
        }
    }

    fun filterData(): String {
        var books = books.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterData(bookName = bookName.value).collect{
                books = it
            }
        }
        return books.toList().filter {
            bookName.value == it.bookName
        }.sumOf { it.quantityAdd }.toString()
    }
}