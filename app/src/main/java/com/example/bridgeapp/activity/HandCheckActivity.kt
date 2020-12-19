package com.example.bridgeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bridgeapp.R
import com.example.bridgeapp.logic.DealStageState
import com.example.bridgeapp.structure.RubberObject

class HandCheckActivity : AppCompatActivity() {

    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hand_check)
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

        if(rubber.latestGame.latestPlay.dealHistory!!.checkHands()){
            intent = Intent (this, StartBidStageActivity::class.java)
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val startOver = findViewById<View>(R.id.dealStartOverButton) as Button

        startOver.setOnClickListener {
            rubber.latestGame.latestPlay.dealHistory = DealStageState(rubber.latestGame.latestPlay.dealingPlayer)
            intent = Intent (this, PlayerHandActivity::class.java)
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}