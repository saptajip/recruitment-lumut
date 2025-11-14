package com.example.saptajiprasetyo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.saptajiprasetyo.ui.viewModel.PostViewModel
import com.example.saptajiprasetyo.utils.Resource

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gunakan layout tablet atau hp secara otomatis
        setContentView(R.layout.activity_main)

        // Hanya tambahkan fragment pertama kali
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

    // Fungsi untuk mendeteksi tablet
    private fun isTabletDevice(): Boolean {
        val metrics = resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        val smallestWidthDp = minOf(widthDp, heightDp)

        return smallestWidthDp >= 600 // tablet threshold
    }

    // Dipanggil ketika user memilih item di fragment list
    fun onItemSelected(item: String) {
        if (isTablet) {
            // Update detail fragment (tablet)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail, DetailFragment.newInstance(item))
                .commit()
        } else {
            // Buka activity detail (HP)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }
    }
}
