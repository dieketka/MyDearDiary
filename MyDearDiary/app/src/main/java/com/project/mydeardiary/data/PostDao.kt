package com.project.mydeardiary.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
//interface which manipulates with data from DB
@Dao
interface PostDao {

    fun getTasks(query: String, sortOrder: SortOrder): Flow<List<Post>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query)
            SortOrder.BY_NAME -> getTasksSortedByName(query)

        }


        @Query("SELECT * FROM post_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name")
    fun getTasksSortedByName(searchQuery: String): Flow<List<Post>>

    @Query("SELECT * FROM post_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY created")
    fun getTasksSortedByDateCreated(searchQuery: String): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //defines what to do if the same task is inserted
    suspend fun insert(task: Post) // Insert function in app db

    @Update
    suspend fun update(task: Post) // Update function in app db

    @Delete
    suspend fun delete(task: Post) // Delete function in app db
}