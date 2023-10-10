package com.project.mydeardiary.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.android.gms.gcm.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post_table" ) //returning right table
    fun getPosts(): Flow<List<Task>>  //a stream of data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Post) //creating Insert function in app db

    @Update
    suspend fun update(task: Task) //creating Update function in app db

    @Delete
    suspend fun delete(task: Task) //creating Delete function in app db
}