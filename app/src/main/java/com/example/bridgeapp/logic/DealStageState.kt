package com.example.bridgeapp.logic

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.structure.HandObject
import java.util.*
import java.io.Serializable

class DealStageState : Parcelable, Serializable {
    val hands: Array<HandObject?>?
    private var cards: MutableList<String>? = ArrayList()
    var currentPlayer: Int
        private set
    private var finished: Boolean

    constructor(currentPlayer: Int) {
        this.currentPlayer = currentPlayer
        hands = arrayOfNulls(4)
        finished = false
    }

    fun setHandForCurrentPlayer(hand: HandObject?) {
        setHandForPlayer(hand, currentPlayer)
    }

    private fun setHandForPlayer(hand: HandObject?, player: Int) {
        hands!![player] = hand
    }

    private fun playerHandCorrect(player: Int): Boolean {
        for (i in 1..2) {
            val checked = (i + player) % 4
            for (s in hands!![player]!!.suits!!) {
                for (c in s!!.cards) {
                    if (c != null && hands[checked]!!.hasCard(s.suit, c)) return false
                }
            }
        }
        return true
    }

    fun checkHands(): Boolean {
        for (i in 0..2) {
            if (!playerHandCorrect(i)) return false
        }
        finished = true
        return true
    }

    fun switchPlayer() {
        currentPlayer = (currentPlayer + 1) % 4
    }

    val cardList: List<String>?
        get() = cards

    fun addCard(card: String) {
        cards!!.add(card)
    }

    fun removeCard(card: String?) {
        cards!!.remove(card)
    }

    fun clearCardList() {
        cards!!.clear()
    }

    fun cardCount(): Int {
        return cards!!.size
    }

    fun isFinished(): Boolean {
        checkHands()
        return finished
    }

    //Parcelable implementation
    private constructor(`in`: Parcel) {
        hands = `in`.createTypedArray(HandObject.CREATOR)
        cards = `in`.createStringArrayList()
        currentPlayer = `in`.readInt()
        finished = `in`.readByte().toInt() != 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedArray(hands, flags)
        dest.writeStringList(cards)
        dest.writeInt(currentPlayer)
        dest.writeByte((if (finished) 1 else 0).toByte())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<DealStageState> = object : Parcelable.Creator<DealStageState> {
            override fun createFromParcel(`in`: Parcel): DealStageState {
                return DealStageState(`in`)
            }

            override fun newArray(size: Int): Array<DealStageState?> {
                return arrayOfNulls(size)
            }
        }
    }
}