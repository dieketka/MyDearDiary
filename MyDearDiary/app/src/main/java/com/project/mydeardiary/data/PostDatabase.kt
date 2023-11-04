package com.project.mydeardiary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

annotation class ApplicationScope

@Database(entities = [Post::class], version =1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao


    class Callback @Inject constructor(
        private val database: Provider<PostDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().postDao()
            applicationScope.launch {
                dao.insert(Post("This is my first post"))
                dao.insert(Post("This is my second post"))
                dao.insert(Post("Test test test test testtest  test  test test test test test test test test test test "))
                dao.insert(Post("Test2"))



        }
    }

    }
}