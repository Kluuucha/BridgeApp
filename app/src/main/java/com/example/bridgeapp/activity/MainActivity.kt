package com.example.bridgeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val start = findViewById<View>(R.id.startAppButton) as Button

        start.setOnClickListener {
            intent = Intent (this, StartRubberActivity::class.java)
            startActivity(intent)
        }
    }
}