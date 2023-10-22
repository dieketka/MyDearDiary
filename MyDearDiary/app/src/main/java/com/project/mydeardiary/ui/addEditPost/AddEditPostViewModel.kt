package com.project.mydeardiary.ui.addEditPost

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.data.PostDao
import dagger.assisted.Assisted


class AddEditPostViewModel @ViewModelInject constructor(
    private val postDao: PostDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    var post = state.get<Post>("post")
    var postName = state.get<String>("postName") ?: post?.name ?: ""
        set(value) {
            field = value
            state.set("postName", value)
        }
}
