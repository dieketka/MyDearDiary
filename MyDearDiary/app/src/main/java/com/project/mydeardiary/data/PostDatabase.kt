package com.project.mydeardiary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.gcm.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

annotation class ApplicationScope

@Database(entities = [Task::class], version = 1)
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
                dao.insert(Post("Test"))
                dao.insert(Post("Test1"))
                dao.insert(Post("Test2"))

        }
    }

    }
}