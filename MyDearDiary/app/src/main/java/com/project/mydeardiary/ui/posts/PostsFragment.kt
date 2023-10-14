package com.project.mydeardiary.ui.posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.gcm.Task
import com.project.mydeardiary.R
import com.project.mydeardiary.databinding.FragmentMainBinding
import com.project.mydeardiary.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment: Fragment(R.layout.fragment_main) {
    private val viewModel: PostsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        val postsAdapter = PostsAdapter()
        binding.apply {
            mainRecyclerView.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            postsAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu)

        val searchButton = menu.findItem(R.id.search)
        val searchView = searchButton.actionView as SearchView

        searchView.onQueryTextChanged {
            //
        }
    }

    }


private fun PostsAdapter.submitList(it: List<Task>?) {
    TODO("Not yet implemented")
}
