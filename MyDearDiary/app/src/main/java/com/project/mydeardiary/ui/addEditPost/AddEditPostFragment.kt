package com.project.mydeardiary.ui.addEditPost

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import kotlinx.coroutines.launch

//Fragment class for fragment_edit_post.xml file, that holds the binding information for the fragment
@AndroidEntryPoint
class addEditPostFragment : Fragment(R.layout.fragment_edit_post) {
    private val viewModel: addEditPostViewModel by viewModels()  //injecting ViewModel
    @SuppressLint("SetText", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {  //method is called when the layout appearance is instanciated
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditPostBinding.bind(view) //binding the necessary values
        binding.apply {
            etEditPost.setText(viewModel.postName)
            tvCreated.isVisible = viewModel.post != null
            tvCreated.text = "Created: ${viewModel.post?.createdDateFormatted}"

            etEditPost.addTextChangedListener{
                viewModel.postName = it.toString()
            }

            editButton.setOnClickListener{ //from fragment_edit_post.xml button
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addEditPostEvent.collect{
                event ->
                when(event){

                    is addEditPostViewModel.AddEditPostEvent.ShowInvalidInputMessage -> {

                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }

                    is addEditPostViewModel.AddEditPostEvent.NavigateBackWithResult ->{

                        binding.etEditPost.clearFocus() // hides keyboard
                        setFragmentResult(
                            "add_edit_request" ,
                                    bundleOf("add_edit_result" to event.res)
                        )
                        findNavController().popBackStack()

                        }


                }.exhaustive
            }
        }
    }
}
