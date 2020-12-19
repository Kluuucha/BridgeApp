package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.util.CardSuit
import com.example.bridgeapp.util.CardValue

class SuitObject : Parcelable {
    val suit: CardSuit
    val cards: Array<CardValue?>

    constructor(suit: CardSuit) {
        this.suit = suit
        cards = arrayOfNulls(CardValue.values().size)
    }

    fun addCard(card: CardValue) {
        if (!hasCard(card)) {
            cards[card.ordinal] = card
        }
    }

    fun hasCard(card: CardValue?): Boolean {
        return listOf(*cards).contains(card)
    }

    //Parcelable implementation
    private constructor(`in`: Parcel) {
        suit = CardSuit.valueOf(`in`.readString()!!)
        val cardNames = `in`.createStringArray()
        cards = arrayOfNulls(cardNames!!.size)
        for (i in cardNames.indices) {
            if (cardNames[i] != null) cards[i] = CardValue.valueOf(cardNames[i]!!)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        val cardNames = arrayOfNulls<String>(cards.size)
        for (i in cardNames.indices) {
            if (cards[i] != null) cardNames[i] = cards[i]!!.name
        }
        dest.writeString(suit.name)
        dest.writeStringArray(cardNames)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<SuitObject?> = object : Parcelable.Creator<SuitObject?> {
            override fun createFromParcel(`in`: Parcel): SuitObject? {
                return SuitObject(`in`)
            }

            override fun newArray(size: Int): Array<SuitObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}