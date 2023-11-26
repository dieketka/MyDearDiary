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
import androidx.lifecycle.Lifecycle
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
//Fragment class for fragment_main.xml file, that holds the binding information for the fragment
 class PostsFragment : Fragment(R.layout.fragment_main), PostsAdapter.OnItemClickListener, MenuProvider {
    private val viewModel: PostsViewModel by viewModels() //injecting ViewModel
    private lateinit var searchView: SearchView



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //method is called when the layout appearance is instanciated
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        val postsAdapter = PostsAdapter(this)
        binding.apply {
            mainRecyclerView.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(requireContext()) //is responsible how the Recycler View layouts items on the screen
                setHasFixedSize(true) //RecyclerView does not change its dimensions on the screen
            }



            //defines what happens when swiping posts right or left
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val post = postsAdapter.currentList[viewHolder.adapterPosition] //reference to current post
                    viewModel.onPostSwiped(post) //delegating to ViewModel
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
            postsAdapter.submitList(it)  //any updates
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.postsEvent.collect{ //when this event started
                event ->
                when (event) {
                    is PostsViewModel.PostsEvent.ShowUndoDeletePostMessage ->{ //show this message
                        Snackbar.make(requireView(), getString(R.string.post_deleted), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo)) { // when Undo is clicked
                                viewModel.onUndoDeleteClick(event.post) //restore the event
                            }.show() // displaying snackBar
                    }

                    is PostsViewModel.PostsEvent.NavigateToAddPostScreen -> {  //nav.xml
                      val action = PostsFragmentDirections.actionPostsFragmentToAddEditPostFragment(getString(
                                                R.string.new_post1), null)
                        findNavController().navigate(action)
                    }
                    is PostsViewModel.PostsEvent.NavigateToEditPostScreen -> { //nav.xml
                    val action = PostsFragmentDirections.actionPostsFragmentToAddEditPostFragment(getString(
                                            R.string.edit_post), event.post)
                        findNavController().navigate(action)

                    }

                    is PostsViewModel.PostsEvent.ShowPostSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg2, Snackbar.LENGTH_SHORT).show()
                    }

                }.exhaustive
            }
        }

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onItemClick(post: Post) {
        viewModel.onPostSelected(post)
    }


    override fun onCreateMenu(menu: Menu,  inflater: MenuInflater) { //activating menu
        inflater.inflate(R.menu.menu_fragment_main, menu) // navigation from menu_fragment_main_xml

        val searchButton = menu.findItem(R.id.search) //refference to search button
        searchView = searchButton.actionView as SearchView //refference to search view

        val pendingQuery = viewModel.searchQuery.value //keeping searchview active when turning device or minimalizing it
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

            //calling method from ViewModel
            R.id.action_sort_by_name -> {  //when item is clicked
                viewModel.onSortOrderSelected(SortOrder.BY_NAME) //this happens

                true
            }

            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)

                true
            }

            else -> super.onContextItemSelected(item) //the Else case needs to be added, this should not be called
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}

