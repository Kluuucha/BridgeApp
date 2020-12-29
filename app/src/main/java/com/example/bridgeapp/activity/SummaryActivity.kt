package com.example.bridgeapp.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.GameObject
import com.example.bridgeapp.structure.PlayObject
import com.example.bridgeapp.structure.PointObject
import com.example.bridgeapp.structure.RubberObject
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream


class SummaryActivity : AppCompatActivity() {

    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
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

        val aboveTable = findViewById<View>(R.id.aboveLineTable) as TableLayout
        val belowTable = findViewById<View>(R.id.belowLineTable) as TableLayout

        val next = findViewById<View>(R.id.summaryNextButton) as Button

        var newGame = false
        var newPlay = false

        for(g: GameObject in rubber.games!!){
            for(p: PlayObject in g.plays!!){
                if(!p.isNoPointPlay){
                    val rowAbove = TableRow(this)
                    val scoreAbove0 = TextView(this)
                    scoreAbove0.textSize = (14).toFloat()
                    scoreAbove0.gravity = Gravity.CENTER_HORIZONTAL
                    scoreAbove0.layoutParams = TableRow.LayoutParams(0)

                    val scoreAbove1 = TextView(this)
                    scoreAbove1.textSize = (14).toFloat()
                    scoreAbove1.gravity = Gravity.CENTER_HORIZONTAL
                    scoreAbove1.layoutParams = TableRow.LayoutParams(1)

                    if(p.contract!!.player % 2 == 0){
                        scoreAbove0.text = PointObject.getTotal(p.pointHistory!!.pointsAboveLine).toString()
                        scoreAbove1.text = PointObject.getTotal(p.pointHistory!!.pointsDefence).toString()
                    }
                    else if (p.contract!!.player % 2 == 1){
                        scoreAbove1.text = PointObject.getTotal(p.pointHistory!!.pointsAboveLine).toString()
                        scoreAbove0.text = PointObject.getTotal(p.pointHistory!!.pointsDefence).toString()
                    }
                    rowAbove.addView(scoreAbove0)
                    rowAbove.addView(scoreAbove1)
                    aboveTable.addView(rowAbove)

                    if(PointObject.getTotal(p.pointHistory!!.pointsBelowLine) > 0) {
                        val rowBelow = TableRow(this)
                        val scoreBelow0 = TextView(this)
                        scoreBelow0.textSize = (14).toFloat()
                        scoreBelow0.gravity = Gravity.CENTER_HORIZONTAL
                        scoreBelow0.layoutParams = TableRow.LayoutParams(0)

                        val scoreBelow1 = TextView(this)
                        scoreBelow1.textSize = (14).toFloat()
                        scoreBelow1.gravity = Gravity.CENTER_HORIZONTAL
                        scoreBelow1.layoutParams = TableRow.LayoutParams(1)

                        if (p.contract!!.player % 2 == 0) {
                            scoreBelow0.text =
                                PointObject.getTotal(p.pointHistory!!.pointsBelowLine).toString()
                            scoreBelow1.text = "0"
                            scoreBelow1.visibility = View.INVISIBLE
                        } else if (p.contract!!.player % 2 == 1) {
                            scoreBelow1.text =
                                PointObject.getTotal(p.pointHistory!!.pointsBelowLine).toString()
                            scoreBelow0.text = "0"
                            scoreBelow0.visibility = View.INVISIBLE
                        }
                        rowBelow.addView(scoreBelow0)
                        rowBelow.addView(scoreBelow1)
                        belowTable.addView(rowBelow)
                    }
                }
            }
            g.winner

            if(g.isFinished){

                val lineRow = View(this)
                lineRow.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ((if(rubber.isFinished()) 6 else 2) * resources.displayMetrics.density).toInt()
                )
                lineRow.setBackgroundColor(Color.parseColor("#C0C0C0"))

                belowTable.addView(lineRow)

                if(rubber.isFinished()){
                    next.visibility = View.INVISIBLE
                    val rowFinal = TableRow(this)
                    val scoreFinal0 = TextView(this)
                    scoreFinal0.textSize = (18).toFloat()
                    scoreFinal0.gravity = Gravity.CENTER_HORIZONTAL
                    scoreFinal0.setTypeface(null, Typeface.BOLD)
                    scoreFinal0.layoutParams = TableRow.LayoutParams(0)

                    val scoreFinal1 = TextView(this)
                    scoreFinal1.textSize = (18).toFloat()
                    scoreFinal1.gravity = Gravity.CENTER_HORIZONTAL
                    scoreFinal1.setTypeface(null, Typeface.BOLD)
                    scoreFinal1.layoutParams = TableRow.LayoutParams(1)

                    val scores = rubber.scores!!
                    scoreFinal0.text = scores[0].toString()
                    scoreFinal1.text = scores[1].toString()

                    rowFinal.addView(scoreFinal0)
                    rowFinal.addView(scoreFinal1)

                    belowTable.addView(rowFinal)
                }
                else{
                    newGame = true
                }
            }
            else{
                newPlay = true
            }
        }

        if(newGame)
            rubber.startNewGame()
        else if (newPlay)
            rubber.latestGame.startNewPlay()


        next.setOnClickListener {
            intent = Intent (this, NewPlayActivity::class.java)
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