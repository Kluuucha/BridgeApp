package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.util.CardSuit
import com.example.bridgeapp.util.CardValue

class HandObject : Parcelable {
    val suits: Array<SuitObject?>?

    constructor() {
        suits = arrayOfNulls(CardSuit.values().size - 1)
        for (i in suits.indices) {
            suits[i] = SuitObject(CardSuit.values()[i])
        }
    }

    fun addCard(suit: CardSuit, value: CardValue) {
        if (!hasCard(suit, value)) {
            suits!![suit.ordinal]!!.addCard(value)
        }
    }

    fun hasCard(suit: CardSuit?, value: CardValue?): Boolean {
        return suits!![suit!!.ordinal]!!.hasCard(value)
    }

    fun honorBonus(trump: CardSuit?): Int {
        if (trump == CardSuit.NOTRUMP) {
            for (s in suits!!) {
                if (!s!!.hasCard(CardValue.ACE)) return 0
            }
            return 2
        } else {
            var trumps = 0
            for (c in suits!![trump!!.ordinal]!!.cards) {
                if (c != null && c.ordinal >= 8) trumps++
            }
            if (trumps - 3 > 0) return trumps - 3
        }
        return 0
    }

    //Parcelable implementation
    private constructor(`in`: Parcel) {
        suits = `in`.createTypedArray(SuitObject.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedArray(suits, flags)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<HandObject?> = object : Parcelable.Creator<HandObject?> {
            override fun createFromParcel(`in`: Parcel): HandObject? {
                return HandObject(`in`)
            }

            override fun newArray(size: Int): Array<HandObject?> {
                return arrayOfNulls(size)
            }
        }

        fun fromStringArray(array: Array<String>): HandObject {
            val hand = HandObject()
            for (s in array) {
                val card = s.split("_").toTypedArray()
                hand.addCard(CardSuit.valueOf(card[1]), CardValue.valueOf(card[0]))
            }
            return hand
        }
    }
}