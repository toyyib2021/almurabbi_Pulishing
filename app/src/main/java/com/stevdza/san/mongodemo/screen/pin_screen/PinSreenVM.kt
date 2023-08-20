package com.stevdza.san.mongodemo.screen.pin_screen

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza.san.mongodemo.data.dataStore.PassKey
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class PinScreenVM : ViewModel(){

    private val _password =
        MutableStateFlow<String>("")
    val password: StateFlow<String> = _password


    private val _onPasscodeConfirmed = MutableSharedFlow<Unit>()
    private val _onPasscodeRejected = MutableSharedFlow<Unit>()

    private val _filledDots = MutableStateFlow(0)
//    val password =   mutableStateOf("")


    var confirmPasscode: StringBuilder = StringBuilder()

    val onPasscodeConfirmed = _onPasscodeConfirmed.asSharedFlow()
    val onPasscodeRejected = _onPasscodeRejected.asSharedFlow()

    val filledDots = _filledDots.asStateFlow()

    init {
        resetData()
    }


    private fun emitFilledDots(filledDots: Int) = viewModelScope.launch {
        _filledDots.emit(filledDots)
    }

    private fun emitOnPasscodeConfirmed() = viewModelScope.launch {
        _onPasscodeConfirmed.emit(Unit)
    }

    private fun emitOnPasscodeRejected() = viewModelScope.launch {
        _onPasscodeRejected.emit(Unit)
    }

    private fun resetData() {
        emitFilledDots(0)
        confirmPasscode.clear()
    }

    fun getSavedPassword(context: Context){
        val passKey = PassKey(context)
        try {
            viewModelScope.launch {
                val getPassKey = passKey.getKey.collect{
                    _password.value = it
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getSavedPassword: $e", )
        }

    }

    fun enterKey(key: String) {
        if (_filledDots.value >= PinCreationScreenVM.PASSCODE_LENGTH) {
            return
        }
        val b = confirmPasscode.append(key)
        Log.e(TAG, "b: $b", )
        emitFilledDots(confirmPasscode.length)

        if (_filledDots.value == PASSCODE_LENGTH) {
            if (password.value == confirmPasscode.toString()) {
                emitOnPasscodeConfirmed()
            } else {
                emitOnPasscodeRejected()
                // on wrong shake and show alert and then reset state
            }

        }
    }

    fun deleteKey() {
        if (confirmPasscode.isNotEmpty()) {
            _filledDots.tryEmit(confirmPasscode.deleteAt(confirmPasscode.length - 1).length)
        }
    }



    fun restart() = resetData()

    companion object {
        const val PASSCODE_LENGTH = 6

    }
}