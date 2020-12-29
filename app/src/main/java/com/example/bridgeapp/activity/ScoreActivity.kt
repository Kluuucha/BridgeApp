package com.example.bridgeapp.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.PointObject
import com.example.bridgeapp.structure.RubberObject
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream

class ScoreActivity : AppCompatActivity() {

    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
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

        val table = findViewById<View>(R.id.scoreOveviewTable) as TableLayout

        val next = findViewById<View>(R.id.scoreNextButton) as Button

        if(!rubber.latestGame.latestPlay.isNoPointPlay) {

            table.addView(getLabel(0))

            for (p: PointObject in rubber.latestGame.latestPlay.pointHistory!!.pointsBelowLine!!) {
                val row = TableRow(this)

                val label = TextView(this)
                label.text = p.label
                label.textSize = (14).toFloat()
                label.setTypeface(null, Typeface.BOLD)
                label.layoutParams = TableRow.LayoutParams(0)
                row.addView(label)

                val score = TextView(this)
                score.text = p.points.toString()
                score.textSize = (14).toFloat()
                score.setTypeface(null, Typeface.BOLD)
                score.layoutParams = TableRow.LayoutParams(1)
                row.addView(score)

                table.addView(row)
            }
            for (p: PointObject in rubber.latestGame.latestPlay.pointHistory!!.pointsAboveLine!!) {
                val row = TableRow(this)

                val label = TextView(this)
                label.text = p.label
                label.textSize = (14).toFloat()
                label.layoutParams = TableRow.LayoutParams(0)
                row.addView(label)

                val score = TextView(this)
                score.text = p.points.toString()
                score.textSize = (14).toFloat()
                score.layoutParams = TableRow.LayoutParams(1)
                row.addView(score)

                table.addView(row)
            }

            table.addView(getSpace())

            table.addView(getLabel(1))

            if (rubber.latestGame.latestPlay.pointHistory!!.pointsDefence!!.isNotEmpty())
                for (p: PointObject in rubber.latestGame.latestPlay.pointHistory!!.pointsDefence!!) {
                    val row = TableRow(this)

                    val label = TextView(this)
                    label.text = p.label
                    label.textSize = (14).toFloat()
                    label.layoutParams = TableRow.LayoutParams(0)
                    row.addView(label)

                    val score = TextView(this)
                    score.text = p.points.toString()
                    score.textSize = (14).toFloat()
                    score.layoutParams = TableRow.LayoutParams(1)
                    row.addView(score)

                    table.addView(row)
                }
            else {
                val row = TableRow(this)

                val label = TextView(this)
                label.text = resources.getString(R.string.no_points)
                label.textSize = (14).toFloat()
                label.layoutParams = TableRow.LayoutParams(0)
                row.addView(label)

                table.addView(row)
            }
        }
        else {
            val row = TableRow(this)

            val label = TextView(this)
            label.text = resources.getString(R.string.no_points)
            label.textSize = (14).toFloat()
            label.layoutParams = TableRow.LayoutParams(0)
            row.addView(label)

            table.addView(row)
        }

        next.setOnClickListener {
            intent = Intent (this, SummaryActivity::class.java)
            intent.putExtra("rubber_data", rubber as Parcelable)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun getLabel(team: Int): TableRow {
        val row = TableRow(this)
        val label = TextView(this)

        if(team == 0){
            val contractorText = "${rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.contract.player % 2]}/${rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.contract.player % 2 + 2]}"
            val out = "${resources.getString(R.string.side_declarer)}($contractorText):"
            label.text = out
        }
        else{
            val defenderText = "${rubber.playerNames[(rubber.latestGame.latestPlay.bidHistory!!.contract.player + 1) % 2]}/${rubber.playerNames[(rubber.latestGame.latestPlay.bidHistory!!.contract.player + 1) % 2 + 2]}"
            val out = "${resources.getString(R.string.side_defender)}($defenderText):"
            label.text = out
        }

        label.textSize = (18).toFloat()
        label.setTypeface(null, Typeface.BOLD)
        label.layoutParams = TableRow.LayoutParams(0)

        row.addView(label)
        return row
    }

    private fun getSpace(): TableRow {
        val row = TableRow(this)
        val label = TextView(this)
        label.height = 25

        row.addView(label)
        return row
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