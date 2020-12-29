package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.util.CardSuit
import java.io.Serializable

class ContractObject : Parcelable, Serializable {
    val number: Int
    val suit: CardSuit?
    val doubleVal: Int
    val player: Int

    @JvmOverloads
    constructor(number: Int = 0, suit: CardSuit? = CardSuit.CLUBS, doubleVal: Int = 0, player: Int = 0) {
        this.number = number
        this.suit = suit
        this.doubleVal = doubleVal
        this.player = player
    }

    override fun toString(): String {
        val out = StringBuilder(number.toString())
        out.append(suit!!.symbol)
        for (i in 0 until doubleVal) out.append("X")
        return out.toString()
    }

    private constructor(`in`: Parcel) {
        number = `in`.readInt()
        suit = CardSuit.valueOf(`in`.readString()!!)
        doubleVal = `in`.readInt()
        player = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(number)
        dest.writeString(suit!!.name)
        dest.writeInt(doubleVal)
        dest.writeInt(player)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ContractObject?> = object : Parcelable.Creator<ContractObject?> {
            override fun createFromParcel(`in`: Parcel): ContractObject? {
                return ContractObject(`in`)
            }

            override fun newArray(size: Int): Array<ContractObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}