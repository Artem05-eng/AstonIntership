package com.example.studio42.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studio42.databinding.ActivityMainBinding
import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.util.ActivityListener
import com.example.studio42.util.Listener
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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