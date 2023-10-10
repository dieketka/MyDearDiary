package com.project.mydeardiary.di

import android.app.Application
import androidx.room.Room
import com.project.mydeardiary.data.ApplicationScope
import com.project.mydeardiary.data.PostDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDB(
        app: Application,
        callback: PostDatabase.Callback
    )
    = Room.databaseBuilder(app, PostDatabase::class.java, "post_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun providePostDao(db: PostDatabase) = db.postDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
    }
@Retention(AnnotationRetention.RUNTIME)
@Qualifier

annotation class ApplicationComponent
