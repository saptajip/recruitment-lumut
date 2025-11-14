package com.example.saptajiprasetyo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.saptajiprasetyo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getStringExtra("item") ?: "-"

        binding.textDetail.text = "You selected: $item"
    }
}
