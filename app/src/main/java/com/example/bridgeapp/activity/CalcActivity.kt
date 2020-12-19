package com.example.bridgeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.logic.PointStageState
import com.example.bridgeapp.structure.ContractObject
import com.example.bridgeapp.structure.RubberObject
import com.example.bridgeapp.util.CardSuit
import com.example.bridgeapp.util.GameStage

class CalcActivity : AppCompatActivity() {

    private var rubber = RubberObject()
    private var contract = ContractObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)
        rubber.playerNames = resources.getStringArray(R.array.players)
    }

    override fun onResume() {
        super.onResume()

        val extras:Bundle? = intent.extras

        if (extras != null) {
            if(extras.containsKey("rubber_data")) {
                rubber = (extras.get("rubber_data") as RubberObject)
                rubber.latestGame.latestPlay.pointHistory = PointStageState()
            }
        }

        rubber.latestGame.latestPlay.stage = GameStage.SCORING

        val contractText = findViewById<View>(R.id.calcContractText) as TextView

        val spinnerRow = findViewById<View>(R.id.spinnerRow) as TableRow
        val doubleRow = findViewById<View>(R.id.doubleRow) as TableRow

        val suit = findViewById<View>(R.id.spinner) as Spinner
        val numbers = findViewById<View>(R.id.spinner2) as Spinner

        val isDouble = findViewById<View>(R.id.doubleCheck) as CheckBox
        val isRedouble = findViewById<View>(R.id.redoubleCheck) as CheckBox

        val tricks = findViewById<View>(R.id.editTextNumber) as EditText

        val outcome = findViewById<View>(R.id.outcome) as TextView
        val overtricks = findViewById<View>(R.id.overtickOutcome) as TextView
        val mishaps = findViewById<View>(R.id.mishapOutcome) as TextView

        val calculate: Button = findViewById<View>(R.id.calculate) as Button
        val next: Button = findViewById<View>(R.id.overviewButton) as Button

        if (extras != null) {
            if(extras.containsKey("rubber_data")) {
                numbers.setSelection(rubber.latestGame.latestPlay.bidHistory.contract.number - 1)
                suit.setSelection(rubber.latestGame.latestPlay.bidHistory.contract.suit.ordinal)
                if(rubber.latestGame.latestPlay.bidHistory.contract.doubleVal == 1){
                    isDouble.isChecked = true
                    isRedouble.isChecked = false
                }
                else if (rubber.latestGame.latestPlay.bidHistory.contract.doubleVal == 2){
                    isDouble.isChecked = false
                    isRedouble.isChecked = true
                }
                contractText.text = rubber.latestGame.latestPlay.contract.toString()
                contractText.visibility = View.VISIBLE

                spinnerRow.visibility = View.INVISIBLE
                doubleRow.visibility = View.GONE
            }
        }


        isDouble.setOnClickListener {
            if (isRedouble.isChecked)
                isRedouble.toggle()
        }

        isRedouble.setOnClickListener {
            if (isDouble.isChecked)
                isDouble.toggle()
        }

        next.setOnClickListener {
            intent = Intent (this, ScoreActivity::class.java)
            intent.putExtra("rubber_data", rubber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        calculate.setOnClickListener {
            next.visibility = View.VISIBLE

            val tricksTaken = Integer.parseInt(tricks.text.toString())

            if(PointStageState.areTricksCorrect(tricksTaken)) {
                if (extras != null) {
                    if(!extras.containsKey("rubber_data")){
                        var doubleValue = 0
                        if (isRedouble.isChecked)
                            doubleValue = 2
                        else if (isDouble.isChecked)
                            doubleValue = 1

                        contract = ContractObject(Integer.parseInt(numbers.selectedItem.toString()), CardSuit.valueOf(suit.selectedItem.toString()), doubleValue, 0)
                    }
                    else
                        contract = rubber.latestGame.latestPlay.contract
                }
                rubber.latestGame.latestPlay.pointHistory.setPoints(tricksTaken, contract, rubber.getVulnerability(contract.player%2), rubber.getVulnerability((contract.player+1)%2), rubber.winner, rubber.latestGame.latestPlay.hands)

                outcome.text = rubber.latestGame.latestPlay.pointHistory.scoreContract(tricksTaken, contract).toString()
                overtricks.text = rubber.latestGame.latestPlay.pointHistory.scoreOvertricks(tricksTaken, contract, rubber.getVulnerability(contract.player%2)).toString()
                mishaps.text = rubber.latestGame.latestPlay.pointHistory.scoreMishaps(tricksTaken, contract, rubber.getVulnerability(contract.player%2)).toString()
            }
            else
                Toast.makeText(applicationContext, "Taken tricks must be between 0 and 13", Toast.LENGTH_SHORT).show() // TODO: different way of notification
        }
    }
}