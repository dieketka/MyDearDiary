package com.project.mydeardiary.ui.posts


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.data.PostDao
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val PostDao: PostDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val sortBy = MutableStateFlow(SortBy.BY_DATE)

    private val postsEventChannel = Channel<PostsEvent>()
    val postsEvent = postsEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
        sortBy,

    ) { query, sortBy,  ->
        Pair(query, sortBy)
    }.flatMapLatest { (query, sortBy) ->
        PostDao.getTasks(query, sortBy)
    }
    fun onPostSelected(post: Post) = viewModelScope.launch {
        postsEventChannel.send(PostsEvent.NavigateToEditPostScreen(post))
    }

    fun onPostSwiped(post: Post) = viewModelScope.launch {
        PostDao.delete(post)
        postsEventChannel.send(PostsEvent.ShowUndoDeletePostMessage(post))
    }

    fun onUndoDeleteClick(post: Post) = viewModelScope.launch {
        PostDao.insert(post)
    }
    fun onAddNewPostClick() = viewModelScope.launch {
    postsEventChannel.send(PostsEvent.NavigateToAddPostScreen)
    }

    sealed class PostsEvent {
        object NavigateToAddPostScreen : PostsEvent()
        data class NavigateToEditPostScreen(val post: Post) : PostsEvent()
        data class ShowUndoDeletePostMessage(val post: Post) : PostsEvent()
    }
    val posts = tasksFlow.asLiveData()


}

enum class SortBy { BY_NAME, BY_DATE }

