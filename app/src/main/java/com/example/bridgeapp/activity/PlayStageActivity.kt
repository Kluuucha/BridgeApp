package com.example.bridgeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.RubberObject
import com.example.bridgeapp.util.GameStage

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
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}