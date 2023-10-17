package com.project.mydeardiary.data


import android.provider.SyncStateContract.Helpers.insert
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.mydeardiary.ui.posts.SortBy
import kotlinx.coroutines.flow.Flow
import java.nio.file.Files.delete

@Dao
interface PostDao {


    fun getTasks(query: String, sortOrder: SortBy): Flow<List<Post>> =
        when(sortOrder) {
            SortBy.BY_DATE -> getTasksSortedByDateCreated(query)
            SortBy.BY_NAME -> getTasksSortedByName(query)

        }


        @Query("SELECT * FROM post_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name")
    fun getTasksSortedByName(searchQuery: String): Flow<List<Post>>

    @Query("SELECT * FROM post_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY created")
    fun getTasksSortedByDateCreated(searchQuery: String): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Post) //creating Insert function in app db

    @Update
    suspend fun update(task: Post) //creating Update function in app db

    @Delete
    suspend fun delete(task: Post) //creating Delete function in app db
}