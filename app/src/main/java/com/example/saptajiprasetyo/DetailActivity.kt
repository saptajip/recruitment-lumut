package com.example.saptajiprasetyo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.saptajiprasetyo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val item = intent.getStringExtra("item") ?: "-"

        // Buat fragment dan kirim data lewat arguments
        val fragment = DetailFragment().apply {
            arguments = Bundle().apply {
                putString("item", item)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_detail, fragment)
            .commit()
    }
}

