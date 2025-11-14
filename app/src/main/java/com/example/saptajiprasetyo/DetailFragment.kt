package com.example.saptajiprasetyo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.saptajiprasetyo.databinding.FragmentDetailBinding
import com.example.saptajiprasetyo.ui.viewModel.DetailViewModel
import com.example.saptajiprasetyo.utils.Resource

class DetailFragment : Fragment() {

    // Menggunakan ViewModel terpisah untuk Detail
    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ambil Post ID yang dikirim dari MainActivity
        val postId = arguments?.getInt("postId") ?: -1

        if (postId != -1) {
            // 2. Tampilkan ID sementara untuk debugging
            binding.detailTitleTextView.text = "Loading Detail for ID: $postId"

            // 3. Panggil API Fetch
            viewModel.fetchPostDetail(postId)

            // 4. Observasi LiveData Detail
            observePostDetail()
        } else {
            binding.detailTitleTextView.text = "Error: Post ID not found."
        }
    }

    // Fungsi untuk mengobservasi LiveData Detail
    private fun observePostDetail() {
        viewModel.postDetail.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.detailTitleTextView.text = "Loading..."
                    // Sembunyikan konten, tampilkan progress bar
                }
                is Resource.Success -> {
                    val post = resource.data
                    // 5. Tampilkan data dari API ke View
                    binding.detailTitleTextView.text = "ID: ${post?.id}\n\nCompleted: ${post?.completed}"
                    binding.detailContentTextView.text = "Title: ${post?.title}"
                    // Asumsi layout Anda memiliki textDetail dan textUserId.
                }
                is Resource.Error -> {
                    binding.detailTitleTextView.text = "Error: ${resource.message}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_POST_ID = "postId"

        // Perbaikan: Menerima Int dan menggunakan kunci yang benar
        fun newInstance(postId: Int): DetailFragment {
            val frag = DetailFragment()
            frag.arguments = Bundle().apply {
                putInt(ARG_POST_ID, postId)
            }
            return frag
        }
    }
}