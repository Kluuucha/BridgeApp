package com.example.bridgeapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.fragment.EditNamesDialog
import com.example.bridgeapp.structure.RubberObject


class StartRubberActivity : AppCompatActivity(), EditNamesDialog.EditNamesDialogListener {

    private var playerNames = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_rubber)

        playerNames = resources.getStringArray(R.array.players)
    }

    override fun onResume() {
        super.onResume()

        val playerArray = arrayOf(
            findViewById<View>(R.id.rubberPlayerN) as TextView,
            findViewById<View>(R.id.rubberPlayerE) as TextView,
            findViewById<View>(R.id.rubberPlayerS) as TextView,
            findViewById<View>(R.id.rubberPlayerW) as TextView
        )

        for(i in 0..3){
            playerArray[i].text = playerNames[i]
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, playerNames)

        val firstDealer = findViewById<View>(R.id.firstDealSpinner) as Spinner
        firstDealer.adapter = arrayAdapter

        val editNames = findViewById<View>(R.id.rubberEditPlayersButton) as Button
        val start = findViewById<View>(R.id.rubberStartButton) as Button


        editNames.setOnClickListener {
            openNamesDialog()
        }

        start.setOnClickListener {
            val rubber = RubberObject(firstDealer.selectedItemPosition, playerNames)
            intent = Intent (this, NewPlayActivity::class.java)
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, 100)
            finish()
        }
    }

    private fun openNamesDialog(){
        val dialog = EditNamesDialog()
        dialog.show(supportFragmentManager, "edit names")
    }

    override fun applyNames(names: Array<String>?) {
        playerNames = names as Array<String>
        onResume()
    }
}