package com.example.bridgeapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.logic.DealStageState
import com.example.bridgeapp.structure.RubberObject


class NewPlayActivity : AppCompatActivity() {

    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_play)
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

        val startButton = findViewById<View>(R.id.startPlayButton) as Button

        startButton.setOnClickListener {
            rubber.latestGame.latestPlay.dealHistory = DealStageState(rubber.latestGame.latestPlay.dealingPlayer)

            intent = Intent (this, PlayerHandActivity::class.java)
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}