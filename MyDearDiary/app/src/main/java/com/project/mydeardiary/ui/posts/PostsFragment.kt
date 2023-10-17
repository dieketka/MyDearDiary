package com.project.mydeardiary.ui.posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.mydeardiary.R
import com.project.mydeardiary.databinding.FragmentMainBinding
import com.project.mydeardiary.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment: Fragment(R.layout.fragment_main), MenuProvider {
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
        viewModel.tasks.observe(viewLifecycleOwner) {
            postsAdapter.submitList(it)
        }
        activity?.addMenuProvider(this)
    }


    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu)

        val searchButton = menu.findItem(R.id.search)
        val searchView = searchButton.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortBy.value = (SortBy.BY_NAME)

                true
            }

            R.id.action_sort_by_date_created -> {
                viewModel.sortBy.value = (SortBy.BY_DATE)

                true
            }
            // R.id.action_delete_task -> {

            // true
            else -> super.onOptionsItemSelected(item)
        }


    }

}

