package com.project.mydeardiary.ui.addEditPost

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.mydeardiary.R
import com.project.mydeardiary.databinding.FragmentEditPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditPostFragment : Fragment(R.layout.fragment_edit_post) {
    private val viewModel: AddEditPostViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditPostBinding.bind(view)
        binding.apply {
            etEditPost.setText(viewModel.postName)
            tvCreated.isVisible = viewModel.post != null
            tvCreated.text = "Created: ${viewModel.post?.createdDateFormatted}"
        }
    }
}