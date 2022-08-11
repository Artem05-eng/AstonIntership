package com.example.homework_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.homework_4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}