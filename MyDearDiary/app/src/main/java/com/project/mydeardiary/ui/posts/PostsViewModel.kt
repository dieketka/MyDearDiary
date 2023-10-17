package com.project.mydeardiary.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.mydeardiary.data.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val PostDao: PostDao,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val sortBy = MutableStateFlow(SortBy.BY_DATE)

    private val tasksFlow = combine(
        searchQuery,
        sortBy,

    ) { query, sortBy,  ->
        Pair(query, sortBy)
    }.flatMapLatest { (query, sortBy) ->
        PostDao.getTasks(query, sortBy)
    }


    val tasks = tasksFlow.asLiveData()
}
enum class SortBy { BY_NAME, BY_DATE }

