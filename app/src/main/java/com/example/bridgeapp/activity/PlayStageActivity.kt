package com.example.bridgeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.RubberObject
import com.example.bridgeapp.util.GameStage
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream

class PlayStageActivity : AppCompatActivity() {
    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_stage)
        rubber.playerNames = resources.getStringArray(R.array.players)
    }

    override fun onResume() {
        super.onResume()

        val extras:Bundle? = intent.extras

        if (extras != null) {
            if(extras.containsKey("rubber_data")) {
                rubber = (extras.get("rubber_data") as RubberObject)
            }
        }

        rubber.latestGame.latestPlay.stage = GameStage.PLAY

        val next = findViewById<View>(R.id.playPahseNextButton) as Button

        next.setOnClickListener {
            intent = Intent (this, CalcActivity::class.java)
            intent.putExtra("rubber_data", rubber as Parcelable)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        intent = Intent (this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        saveGame(rubber, this::class.java)
        super.onDestroy()
    }

    fun saveGame(rubber: RubberObject, activity: Class<*>) {
        val out: ObjectOutput
        try {
            val outFile = File(filesDir,"rubber_save.data")
            out = ObjectOutputStream(FileOutputStream(outFile))
            out.writeObject(activity)
            out.writeObject(rubber)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}