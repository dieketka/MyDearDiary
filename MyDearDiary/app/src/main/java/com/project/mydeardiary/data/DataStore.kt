package com.project.mydeardiary.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton



//To store SortOrder in JetPack DataStore

private const val TAG = "PreferencesManager"  //log tag for the exception

enum class SortOrder { BY_NAME, BY_DATE }

data class FilterPreferences (val sortOrder: SortOrder)

@Singleton
class DataStore @Inject constructor(@ApplicationContext context: Context) {

     private val Context.dataStore by preferencesDataStore("user_preferences") //creating DataStore where to store these values

     private val dataStore = context.dataStore

    val preferencesFlow = dataStore.data
        .catch { exception -> //if the flow throws an exception(something went wrong when reading the data)
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)  //default value
                FilterPreferences(sortOrder)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder)  {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey ("sort_order") //the name how SORT ORDER is  stored
    }
