package com.project.mydeardiary.ui.addEditPost

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.data.PostDao
import com.project.mydeardiary.ui.AddPostResultOk
import com.project.mydeardiary.ui.EditPostResultOk
import dagger.assisted.Assisted
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class addEditPostViewModel @ViewModelInject constructor(
    private val postDao: PostDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    var post = state.get<Post>("post")
    var postName = state.get<String>("postName") ?: post?.name ?: ""
        set(value) {
            field = value
            state.set("postName", value)
        }
    private val addEditPostEventChannel = Channel<AddEditPostEvent>()
    val addEditPostEvent = addEditPostEventChannel.receiveAsFlow()
    fun onSaveClick() {
        if (postName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return

        }
        if (post != null){
            val updatePost = post!!.copy(name = postName)
            updatePost(updatePost)
        }
        else {
            val newPost = Post(name = postName)
            createPost(newPost)
        }
    }
    private fun createPost(post: Post) = viewModelScope.launch {
        postDao.insert(post)
        addEditPostEventChannel.send(AddEditPostEvent.NavigateBackWithResult(AddPostResultOk))
    }
    private fun updatePost(post: Post) = viewModelScope.launch {
        postDao.update(post)
        addEditPostEventChannel.send(AddEditPostEvent.NavigateBackWithResult(EditPostResultOk))

    }
    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditPostEventChannel.send(AddEditPostEvent.ShowInvalidInputMessage(text))
    }
    sealed class AddEditPostEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditPostEvent()
        data class NavigateBackWithResult(val res: Int): AddEditPostEvent()
    }

}
