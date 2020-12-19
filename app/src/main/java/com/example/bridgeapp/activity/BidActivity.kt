package com.example.bridgeapp.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bridgeapp.R
import com.example.bridgeapp.structure.RubberObject


class BidActivity : AppCompatActivity() {

    private var rubber = RubberObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid)
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

        val suitSpinner = findViewById<View>(R.id.colorBid) as Spinner
        val numberSpinner = findViewById<View>(R.id.numberBid) as Spinner

        val playerArray = arrayOf(
            findViewById<View>(R.id.playerN) as TextView,
            findViewById<View>(R.id.playerE) as TextView,
            findViewById<View>(R.id.playerS) as TextView,
            findViewById<View>(R.id.playerW) as TextView
        )

        val bidArray = arrayOf(
            findViewById<View>(R.id.bidN) as TextView,
            findViewById<View>(R.id.bidE) as TextView,
            findViewById<View>(R.id.bidS) as TextView,
            findViewById<View>(R.id.bidW) as TextView
        )

        val currentBidder = findViewById<View>(R.id.currentBidder) as TextView
        val currentBidderLabel = findViewById<View>(R.id.nowBiddingLabel) as TextView

        val bidButton = findViewById<View>(R.id.bidButton) as Button
        val passButton = findViewById<View>(R.id.passButton) as Button
        val doubleButton = findViewById<View>(R.id.doubleButton) as Button
        val nextButton = findViewById<View>(R.id.nextButton) as Button

        val contract = findViewById<View>(R.id.contractText) as TextView
        val contractor = findViewById<View>(R.id.contractorText) as TextView

        val contractLabel = findViewById<View>(R.id.contractLabel) as TextView
        val contractorLabel = findViewById<View>(R.id.contractorLabel) as TextView

        doubleButton.isEnabled = false

        for(i in 0..3){
            playerArray[i].text = rubber.playerNames[i]
        }
        setColorToPlayers(playerArray)
        currentBidder.text = rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]
        bidButton.setOnClickListener {
            if(rubber.latestGame.latestPlay.bidHistory!!.runBid(Integer.parseInt(numberSpinner.selectedItem.toString()), resources.getStringArray(
                            R.array.card_color
                    )[suitSpinner.selectedItemPosition].toString())){
                if(rubber.latestGame.latestPlay.bidHistory!!.afterDouble())
                    doubleButton.text = resources.getString(R.string.button_double)

                bidArray[rubber.latestGame.latestPlay.bidHistory!!.lastBidder].text = rubber.latestGame.latestPlay.bidHistory!!.lastBid.toString()
                passButton.isEnabled = true
                doubleButton.isEnabled = true
            }
            else {
                Toast.makeText(applicationContext, "The bid has to be greater than previous one", Toast.LENGTH_SHORT).show() // TODO: different way of notification
            }
            setColorToPlayers(playerArray)
            currentBidder.text = rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]
        }

        passButton.setOnClickListener{
            val currentBid = bidArray[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]

            rubber.latestGame.latestPlay.bidHistory!!.runPass()

            currentBid.text = rubber.latestGame.latestPlay.bidHistory!!.lastBid.toString()

            if (rubber.latestGame.latestPlay.bidHistory!!.passOut()) {
                bidButton.isEnabled = false
                passButton.isEnabled = false

                contract.visibility = View.VISIBLE
                contractLabel.visibility = View.VISIBLE
                contractor.visibility = View.VISIBLE
                contractorLabel.visibility = View.VISIBLE
                nextButton.visibility = View.VISIBLE

                currentBidder.visibility = View.INVISIBLE
                currentBidderLabel.visibility = View.INVISIBLE
                suitSpinner.visibility = View.INVISIBLE
                numberSpinner.visibility = View.INVISIBLE
                bidButton.visibility = View.INVISIBLE
                passButton.visibility = View.INVISIBLE
                doubleButton.visibility = View.INVISIBLE

                if(rubber.latestGame.latestPlay.bidHistory!!.hasBids()) {
                    contract.text = rubber.latestGame.latestPlay.bidHistory!!.contract.toString()
                    val contractorText = rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.contract.player%2] + "/" + rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.contract.player%2 + 2]
                    contractor.text = contractorText
                }
                else{
                    rubber.latestGame.latestPlay.setNoPoint(true)
                }
            }

            doubleButton.isEnabled = false

            if(rubber.latestGame.latestPlay.bidHistory!!.afterDouble()){
                doubleButton.text = resources.getString(R.string.button_double)
            }
            setColorToPlayers(playerArray)
            currentBidder.text = rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]
        }

        doubleButton.setOnClickListener{
            if(rubber.latestGame.latestPlay.bidHistory!!.beforeDouble()) {
                doubleButton.text = resources.getString(R.string.button_redouble)
            }

            if(rubber.latestGame.latestPlay.bidHistory!!.afterDouble() && !rubber.latestGame.latestPlay.bidHistory!!.afterRedouble()){
                doubleButton.text = resources.getString(R.string.button_double)
                doubleButton.isEnabled = false
            }

            val currentBid = bidArray[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]

            rubber.latestGame.latestPlay.bidHistory!!.runDouble()

            currentBid.text = rubber.latestGame.latestPlay.bidHistory!!.lastBid.toString()

            setColorToPlayers(playerArray)
            currentBidder.text = rubber.playerNames[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer]
        }

        nextButton.setOnClickListener{
            if(rubber.latestGame.latestPlay.isNoPointPlay){
                intent = Intent (this, ScoreActivity::class.java)
                intent.putExtra("rubber_data", rubber)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            else{
                intent = Intent (this, PlayStageActivity::class.java)
                intent.putExtra("rubber_data", rubber)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setColorToPlayers(playerArray: Array<TextView>) {
        for(player: TextView in playerArray){
            player.setTextColor(Color.BLACK)
        }
        playerArray[rubber.latestGame.latestPlay.bidHistory!!.currentPlayer].setTextColor(Color.BLUE)
    }
}