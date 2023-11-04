package com.project.mydeardiary.ui.posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.mydeardiary.R
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.data.SortOrder
import com.project.mydeardiary.databinding.FragmentMainBinding
import com.project.mydeardiary.util.exhaustive
import com.project.mydeardiary.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

 class PostsFragment : Fragment(R.layout.fragment_main), PostsAdapter.OnItemClickListener, MenuProvider {
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val binding = FragmentMainBinding.bind(view)
        val postsAdapter = PostsAdapter(this)
        binding.apply {
            mainRecyclerView.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true) //RecyclerView does not change its dimensions on the screen
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

        setFragmentResultListener("add_edit_request"){ _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)

        }
        viewModel.post.observe(viewLifecycleOwner) {
            postsAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.postsEvent.collect{
                event ->
                when (event) {
                    is PostsViewModel.PostsEvent.ShowUndoDeletePostMessage ->{
                        Snackbar.make(requireView(), "Post deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                viewModel.onUndoDeleteClick(event.post)
                            }.show()
                    }

                    is PostsViewModel.PostsEvent.NavigateToAddPostScreen -> {  //nav.xml
                      val action = PostsFragmentDirections.actionPostsFragmentToAddEditPostFragment("New post", null)
                        findNavController().navigate(action)
                    }
                    is PostsViewModel.PostsEvent.NavigateToEditPostScreen -> { //nav.xml
                    val action = PostsFragmentDirections.actionPostsFragmentToAddEditPostFragment("Edit post", event.post)
                        findNavController().navigate(action)

                    }

                    is PostsViewModel.PostsEvent.ShowPostSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg2, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        activity?.addMenuProvider(this)
    }

    override fun onItemClick(post: Post) {
        viewModel.onPostSelected(post)
    }


    override fun onCreateMenu(menu: Menu,  inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu) // navigation from menu_fragment_main_xml

        val searchButton = menu.findItem(R.id.search)
        searchView = searchButton.actionView as SearchView
        val pendingQuery = viewModel.searchQuery.value
        if (!pendingQuery.isNullOrEmpty()) {
            searchButton.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean { //from menu_fragment_main_xml
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)

                true
            }

            R.id.action_sort_by_date_created -> {  //from menu_fragment_main_xml
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)

                true
            }

            else -> super.onContextItemSelected(item)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}

