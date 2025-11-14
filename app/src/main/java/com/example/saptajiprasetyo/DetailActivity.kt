package com.example.saptajiprasetyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
// Jika Anda menggunakan ViewBinding di Activity ini:
// import com.example.saptajiprasetyo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Pastikan ID container fragment Anda adalah container_detail
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail"
        // 1. Ambil Post ID yang dikirim dari MainActivity
        // Gunakan getIntExtra() dengan kunci yang benar "POST_ID"
        val postId = intent.getIntExtra("POST_ID", -1)

        Log.d("DetailActivity", "Received Post ID: $postId")

        if (postId != -1) {
            // 2. Buat fragment menggunakan newInstance yang sudah diperbaiki
            val fragment = DetailFragment.newInstance(postId)

            // 3. Tampilkan DetailFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail, fragment) // Pastikan R.id.container_detail adalah ID FrameLayout/container Anda
                .commit()
        } else {
            Log.e("DetailActivity", "Error: POST_ID not found in Intent.")
            // Tampilkan pesan error ke pengguna jika diperlukan
        }
    }
}