package com.example.bridgeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.RubberObject
import java.io.*

class MainActivity : AppCompatActivity() {

    private var rubber = RubberObject()
    private var resumeClass: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val start = findViewById<View>(R.id.startAppButton) as Button
        val resume = findViewById<View>(R.id.resumeGameButton) as Button
        resume.isEnabled = false

        val file = File(filesDir, "rubber_save.data")

        if(file.exists()){
            resume.isEnabled = true
        }

        start.setOnClickListener {
            intent = Intent (this, StartRubberActivity::class.java)
            startActivity(intent)
        }

        resume.setOnClickListener {
            loadGame()
            intent = Intent (this, resumeClass)
            intent.putExtra("rubber_data", rubber as Parcelable)
            startActivity(intent)
        }


    }

    fun loadGame() {
        val input: ObjectInput
        try {
            val outFile = File(filesDir,"rubber_save.data")
            input = ObjectInputStream(FileInputStream(outFile))
            resumeClass = input.readObject() as Class<*>
            rubber = input.readObject() as RubberObject
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}