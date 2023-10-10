package com.project.mydeardiary.ui.posts

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.mydeardiary.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment: Fragment(R.layout.fragment_main) {
    private val viewModel: PostsViewModel by viewModels()
}