package com.example.bridgeapp.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.HandObject
import com.example.bridgeapp.structure.RubberObject
import com.example.bridgeapp.util.CardSuit
import com.example.bridgeapp.util.CardValue
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream

class DealActivity : AppCompatActivity(), View.OnClickListener{

    private var rubber = RubberObject()

    private var cardsChecked: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)
        rubber.playerNames = resources.getStringArray(R.array.players)

        val cardsCount = findViewById<View>(R.id.cardsCounter) as TextView
        val cardCountText = "$cardsChecked/13"
        cardsCount.text = cardCountText

        val setHandButton = findViewById<View>(R.id.setHandButton) as Button
        setHandButton.isEnabled = false

        val table = findViewById<View>(R.id.cardTable) as TableLayout

        for(c in CardValue.values()) {
            val row = TableRow(this)

            val label = TextView(this)
            label.text = c.symbol
            label.textSize = (18).toFloat()
            label.setTypeface(null, Typeface.BOLD)
            label.layoutParams = TableRow.LayoutParams(0)

            label.gravity = Gravity.CENTER_VERTICAL

            row.addView(label)
            for(s in CardSuit.values()){
                if(s != CardSuit.NOTRUMP){
                    val checkbox = CheckBox(this)
                    val checkboxText = "${c.name}_${s.name}"
                    checkbox.text = checkboxText
                    checkbox.width = (32 * resources.displayMetrics.scaledDensity).toInt()
                    checkbox.height = (32 * resources.displayMetrics.scaledDensity).toInt()
                    checkbox.layoutParams = TableRow.LayoutParams(s.ordinal + 1)

                    checkbox.setOnClickListener(this)
                    row.addView(checkbox)
                }
            }
            table.addView(row)
        }
    }

    override fun onResume() {
        super.onResume()

        val extras:Bundle? = intent.extras

        if (extras != null) {
            if(extras.containsKey("rubber_data")) {
                rubber = (extras.get("rubber_data") as RubberObject)
            }
        }

        rubber.latestGame.latestPlay.dealHistory!!.clearCardList()

        val setHandButton = findViewById<View>(R.id.setHandButton) as Button

        setHandButton.setOnClickListener {
            rubber.latestGame.latestPlay.dealHistory!!.setHandForCurrentPlayer(HandObject.fromStringArray(rubber.latestGame.latestPlay.dealHistory!!.cardList!!.toTypedArray()))
            rubber.latestGame.latestPlay.dealHistory!!.switchPlayer()

            if(rubber.latestGame.latestPlay.dealHistory!!.currentPlayer == rubber.latestGame.latestPlay.dealingPlayer){
                intent = Intent (this, HandCheckActivity::class.java)
                intent.putExtra("rubber_data", rubber as Parcelable)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            else{
                intent = Intent (this, PlayerHandActivity::class.java)
                intent.putExtra("rubber_data", rubber as Parcelable)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        val cardsCount = findViewById<View>(R.id.cardsCounter) as TextView
        val setHandButton = findViewById<View>(R.id.setHandButton) as Button

        if(v is CheckBox){
            println(v.text)
            if(!v.isChecked){
                rubber.latestGame.latestPlay.dealHistory!!.removeCard(v.text.toString())
            }
            else{
                if(rubber.latestGame.latestPlay.dealHistory!!.cardCount() >= 13){
                    Toast.makeText(applicationContext, "You cannot check more than 13 cards", Toast.LENGTH_SHORT).show()
                    v.toggle()
                }
                else
                    rubber.latestGame.latestPlay.dealHistory!!.addCard(v.text.toString())
            }
            val cardCountText = rubber.latestGame.latestPlay.dealHistory!!.cardCount().toString() + "/13"
            cardsCount.text = cardCountText

            setHandButton.isEnabled = rubber.latestGame.latestPlay.dealHistory!!.cardCount() == 13
        }
    }

    override fun onBackPressed() {
        intent = Intent (this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}