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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.mydeardiary.R
import com.project.mydeardiary.databinding.FragmentMainBinding
import com.project.mydeardiary.util.exhaustive
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
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val post = postsAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onPostSwiped(post)
                }
            }).attachToRecyclerView(mainRecyclerView)
            addButton.setOnClickListener {
                 viewModel.onAddNewPostClick()
            }
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            postsAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.postsEvent.collect{
                event ->
                when (event) {
                    is PostsViewModel.PostsEvent.ShowUndoDeletePostMessage ->{
                        Snackbar.make(requireView(), "Post deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                viewModel.onUndoDeleteClick(event.post)
                            }.show()

                    }

                    is PostsViewModel.PostsEvent.NavigateToAddPostScreen -> {

                    }
                    is PostsViewModel.PostsEvent.NavigateToEditPostScreen -> {


                    }
                }.exhaustive
            }
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

