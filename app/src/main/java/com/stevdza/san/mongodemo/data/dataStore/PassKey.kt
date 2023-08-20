package com.stevdza.san.mongodemo.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PassKey(private val context: Context) {

    // to make sure there's only one instance
    companion object {
        internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore("PassKey")
        val PASS_KEY = stringPreferencesKey("PassKey")

    }

    //get the saved key
    val getKey = context.dataStore.data
        .map { preferences ->
            preferences[PASS_KEY] ?: ""

        }

    //save key into datastore
    suspend fun saveKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[PASS_KEY] = key
        }
    }


}