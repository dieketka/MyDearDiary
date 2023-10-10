package com.project.mydeardiary.ui.posts

import androidx.lifecycle.ViewModel
import com.project.mydeardiary.data.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val PostDao: PostDao
) : ViewModel() {
}