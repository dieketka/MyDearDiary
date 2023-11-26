package com.project.mydeardiary.data.di

import android.app.Application
import androidx.room.Room
import com.project.mydeardiary.data.PostDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

//a place for instructions how to create dependencies that the project needs
@Module
    @InstallIn
    (SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDB(
        app: Application,
        callback: PostDatabase.Callback
    )
    = Room.databaseBuilder(app, PostDatabase::class.java, "post_database") //instruction how to create db
            .fallbackToDestructiveMigration() //Allows Room to recreate DB  if Migrations  are not found.
            .addCallback(callback)
            .build() //one instance of DB

    @Provides
    fun providePostDao(db: PostDatabase) = db.postDao() //instruction how to create Dao


    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob()) //how to create a coroutineScope, if one coroutin fails, the others will still work due to SupervisorJob
    }
@Retention(AnnotationRetention.RUNTIME)
@Qualifier

annotation class ApplicationComponent //a class that is auto generated, contains dependensies for app
