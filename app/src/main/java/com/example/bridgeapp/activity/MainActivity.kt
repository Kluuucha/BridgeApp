package com.example.bridgeapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.fileOperations.TempRubber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val start = findViewById<View>(R.id.startAppButton) as Button
        val resume = findViewById<View>(R.id.resumeGameButton) as Button
        val delete = findViewById<View>(R.id.deleteGameButton) as Button
        resume.isEnabled = false
        delete.isEnabled = false

        val bidding = findViewById<View>(R.id.biddingAssistantButton) as Button
        val calculator = findViewById<View>(R.id.pointCalculatorButton) as Button

        if(TempRubber.saveExists(filesDir)){
            resume.isEnabled = true
            delete.isEnabled = true
        }

        start.setOnClickListener {
            intent = Intent (this, StartRubberActivity::class.java)
            startActivity(intent)
        }

        resume.setOnClickListener {
            TempRubber.loadRubber(filesDir)
            intent = Intent (this, TempRubber.resumeClass)
            intent.putExtra("rubber_data", TempRubber.rubber as Parcelable)
            startActivity(intent)
        }

        delete.setOnClickListener {//TODO: Add pop-up "Do you really want to delete"
            TempRubber.deleteSave(filesDir)
            resume.isEnabled = false
            delete.isEnabled = false
        }

        bidding.setOnClickListener {
            intent = Intent (this, BidActivity::class.java)
            startActivity(intent)
        }

        calculator.setOnClickListener {
            intent = Intent (this, CalcActivity::class.java)
            startActivity(intent)
        }
    }
}