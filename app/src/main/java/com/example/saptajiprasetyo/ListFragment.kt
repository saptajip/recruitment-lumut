package com.example.saptajiprasetyo

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.activityViewModels

class ListFragment : Fragment() {

    private var listener: OnItemClickListener? = null
    private val viewModel: PostViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    interface OnItemClickListener {
        fun onItemClicked(postId: Int)
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
        adapter = PostAdapter(emptyList()) { postId ->
            Log.d("ClickLog", "Fragment Menerima Title: $postId")
            listener?.onItemClicked(postId)

            // Log ini akan muncul jika listener aktif
            if (listener == null) {
                Log.e("ClickLog", "Listener NULL! MainActivity tidak mengimplementasikan OnItemClickListener.")
            }
        }
        recyclerView.adapter = adapter
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Amati data ASLI HANYA untuk status LOADING/ERROR
        viewModel.posts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Tampilkan progress bar/loading state
                    println("Loading posts...")
                }
                is Resource.Error -> {
                    // Tampilkan pesan error
                    println("Error fetching posts: ${resource.message}")
                }
                is Resource.Success -> {
                    // Tidak perlu memanggil adapter.updateList di sini,
                    // karena filteredPosts akan menanganinya
                }
            }
        })

        // 2. Amati data FILTERED untuk memperbarui List (List<Post> non-Resource)
        viewModel.filteredPosts.observe(viewLifecycleOwner, Observer { filteredList ->
            // filteredList adalah List<Post> yang sudah difilter atau list asli (jika query kosong)
            adapter.updateList(filteredList)
        })

        // Fetch posts hanya sekali jika belum ada
        if (viewModel.posts.value == null) {
            viewModel.fetchPosts()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
