package com.project.mydeardiary.ui.addEditPost

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.mydeardiary.R
import com.project.mydeardiary.databinding.FragmentEditPostBinding
import com.project.mydeardiary.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class addEditPostFragment : Fragment(R.layout.fragment_edit_post) {
    private val viewModel: addEditPostViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditPostBinding.bind(view)
        binding.apply {
            etEditPost.setText(viewModel.postName)
            tvCreated.isVisible = viewModel.post != null
            tvCreated.text = "Created: ${viewModel.post?.createdDateFormatted}"

            editButton.setOnClickListener{
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditPostEvent.collect{
                event ->
                when(event){
                    is addEditPostViewModel.AddEditPostEvent.NavigateBackWithResult ->{

                        binding.etEditPost.clearFocus() // hides keyboard
                        setFragmentResult(
                            "add_edit_request" ,
                                    bundleOf("add_edit_result" to event.res)
                        )
                        findNavController().popBackStack()
                }
                    is addEditPostViewModel.AddEditPostEvent.ShowInvalidInputMessage -> {

                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}
