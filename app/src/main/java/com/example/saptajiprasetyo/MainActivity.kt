package com.example.saptajiprasetyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.appcompat.widget.SearchView
import com.example.saptajiprasetyo.ui.viewModel.PostViewModel
import com.example.saptajiprasetyo.utils.Resource

class MainActivity : AppCompatActivity(), ListFragment.OnItemClickListener {

    // ViewModel yang sama akan digunakan oleh ListFragment untuk memfilter data
    private val viewModel: PostViewModel by viewModels()
    private var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Saptaji Prasetyo"

        setupSearchView()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_list, ListFragment())
                .commit()
        }

        // Deteksi tablet
        isTablet = isTabletDevice()

        // Observasi API
        viewModel.posts.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> println("Loading...")
                is Resource.Success -> println("Success: ${resource.data?.size} posts")
                is Resource.Error -> println("Error: ${resource.message}")
            }
        })

        // Trigger fetch data
        viewModel.fetchPosts()
    }

    // Fungsi baru untuk setup SearchView
    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Dipanggil saat pengguna menekan tombol search
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Biasanya tidak perlu aksi saat submit, filtering dilakukan di onQueryTextChange
                return false
            }

            // Dipanggil setiap kali teks dalam kolom pencarian berubah
            override fun onQueryTextChange(newText: String?): Boolean {
                // Kirim query pencarian ke ViewModel
                Log.e( "onQueryTextChange: ", newText.toString())
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }

    // Fungsi untuk mendeteksi tablet (tetap sama)
    private fun isTabletDevice(): Boolean {
        val metrics = resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        val smallestWidthDp = minOf(widthDp, heightDp)

        return smallestWidthDp >= 600 // tablet threshold
    }

    // FUNGSI INI AKAN DIPANGGIL DARI FRAGMENT
    // PASTIKAN parameter menggunakan Post ID (Int)
    override fun onItemClicked(postId: Int) {
        // Log untuk verifikasi akhir
        Log.d("ClickLog", "MainActivity Menerima Post ID: $postId. Memulai Navigasi...")

        if (isTablet) {
            // Update detail fragment (tablet)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail, DetailFragment.newInstance(postId))
                .commit()
        } else {
            // Buka activity detail (HP)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("POST_ID", postId)
            startActivity(intent)
        }
    }
}