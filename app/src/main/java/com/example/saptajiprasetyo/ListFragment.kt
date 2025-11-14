package com.example.saptajiprasetyo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saptajiprasetyo.ui.viewModel.PostViewModel
import com.example.saptajiprasetyo.utils.Resource

class ListFragment : Fragment() {

    private var listener: OnItemClickListener? = null
    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    interface OnItemClickListener {
        fun onItemClicked(item: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(emptyList()) { postTitle ->
            listener?.onItemClicked(postTitle)
        }
        recyclerView.adapter = adapter
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observasi LiveData posts
        viewModel.posts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    println("Loading posts...")
                }
                is Resource.Success -> {
                    adapter.updateList(resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    println("Error fetching posts: ${resource.message}")
                }
            }
        })

        // Fetch posts dari repository
        viewModel.fetchPosts()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
