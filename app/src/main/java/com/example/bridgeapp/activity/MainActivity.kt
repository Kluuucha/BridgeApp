package com.example.bridgeapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.RubberObject

class MainActivity : AppCompatActivity() {

    private var rubber = RubberObject()
    private var isBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resume = findViewById<View>(R.id.resumeGameButton) as Button
        resume.isEnabled = isBack
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("RESULT RECEIVED")

        val extras:Bundle? = data?.extras

        if (requestCode == 100){
            if (extras != null) {
                if(extras.containsKey("rubber_data")) {
                    rubber = (extras.get("rubber_data") as RubberObject)
                    isBack = true
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        val resume = findViewById<View>(R.id.resumeGameButton) as Button
        resume.isEnabled = isBack
    }

    override fun onResume() {
        super.onResume()

        val start = findViewById<View>(R.id.startAppButton) as Button
        val resume = findViewById<View>(R.id.resumeGameButton) as Button

        start.setOnClickListener {
            intent = Intent (this, StartRubberActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }
}