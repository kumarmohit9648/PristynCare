package com.mohit.pristyncare.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mohit.pristyncare.databinding.ActivityViewBinding

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        Glide.with(this)
            .load(url)
            .into(binding.image)
    }
}