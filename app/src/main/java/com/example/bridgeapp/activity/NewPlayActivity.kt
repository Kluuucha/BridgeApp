package com.example.bridgeapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.fileOperations.TempRubber
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
        TempRubber.saveRubber(filesDir, rubber, this::class.java)
        super.onDestroy()
    }
}