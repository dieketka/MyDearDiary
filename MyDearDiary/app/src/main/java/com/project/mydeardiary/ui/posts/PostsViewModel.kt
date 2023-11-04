package com.project.mydeardiary.ui.posts


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.mydeardiary.data.DataStore
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.data.PostDao
import com.project.mydeardiary.data.SortOrder
import com.project.mydeardiary.ui.AddPostResultOk
import com.project.mydeardiary.ui.EditPostResultOk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val PostDao: PostDao,
    private val preferencesManager: DataStore,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val postsEventChannel = Channel<PostsEvent>()
    val postsEvent = postsEventChannel.receiveAsFlow()

    private val postsFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow

    ) { query, filterPreferences  ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        PostDao.getTasks(query, filterPreferences.sortOrder)
    }
    val post = postsFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
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
    fun onAddEditResult(result: Int){
        when (result) {
            AddPostResultOk -> showPostSavedConformationMessage("Post added")
            EditPostResultOk -> showPostSavedConformationMessage("Post updated")
        }
    }
    fun showPostSavedConformationMessage(text: String) = viewModelScope.launch {
        postsEventChannel.send(PostsEvent.ShowPostSavedConfirmationMessage(text))


}

    sealed class PostsEvent {
        object NavigateToAddPostScreen : PostsEvent()
        data class NavigateToEditPostScreen(val post: Post) : PostsEvent()
        data class ShowUndoDeletePostMessage(val post: Post) : PostsEvent()
        data class ShowPostSavedConfirmationMessage(val msg2: String) : PostsEvent()
    }



}



