package com.project.mydeardiary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

annotation class ApplicationScope

@Database(entities = [Post::class], version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao


    class Callback @Inject constructor(
        private val database: Provider<PostDatabase>,
         private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) { //this method is excecuted the first time DB is opened
            super.onCreate(db)
            val dao = database.get().postDao() //passes posts inside the coroutine
            applicationScope.launch {
                dao.insert(Post("This is your first post"))




        }
    }

    }
}