package com.project.mydeardiary.ui.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.mydeardiary.data.Post
import com.project.mydeardiary.databinding.PostsBinding

//adapter for the RecyclerView(Binds View with data)
class PostsAdapter(private val listener: OnItemClickListener ) : ListAdapter<Post, PostsAdapter.PostsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder { //defining how to instanciate binding classes
        val binding = PostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) { //defines how to bind the data
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class PostsViewHolder(private val binding: PostsBinding) : // automatically generated class due to Binding
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply { //puts the actual data into the dif views
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val post = getItem(position)
                        listener.onItemClick(post)
                    }
                }
            }
        }


        fun bind(post: Post) {
            binding.apply {
                tvPosts.text = post.name
            }
        }
    }

        interface OnItemClickListener {
            fun onItemClick(post: Post)
        }


     class DiffCallback : DiffUtil.ItemCallback<Post>() {
         override fun areItemsTheSame(oldItem: Post, newItem: Post) =
              //if a post is changed, callback updates it
              oldItem.id == newItem.id


         override fun areContentsTheSame(oldItem: Post, newItem: Post) =
              //if anything in a post is changed, callback updates it
              oldItem == newItem


      }

    }
