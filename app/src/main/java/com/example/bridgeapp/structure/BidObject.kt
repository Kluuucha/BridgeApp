package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.util.CardSuit
import java.io.Serializable

class BidObject : Comparable<BidObject>, Parcelable, Serializable {
    val number: Int
    val suit: CardSuit
    val doubleVal: Int
    val isPass: Boolean

    @JvmOverloads
    constructor(number: Int, suit: String, pass: Boolean = false) {
        this.number = number
        println(suit.trim { it <= ' ' })
        this.suit = CardSuit.valueOf(suit.trim { it <= ' ' })
        doubleVal = 0
        isPass = pass
    }

    @JvmOverloads
    constructor(o: BidObject, doubleVal: Int, pass: Boolean = false) {
        number = o.number
        suit = o.suit
        this.doubleVal = doubleVal
        isPass = pass
    }

    constructor(o: BidObject, pass: Boolean) : this(o, 0, pass)

    private val value: Int
        get() = number * 100 + suit.ordinal * 10 + doubleVal

    override fun compareTo(other: BidObject): Int {
        val thisValue = value
        val otherValue = other.value
        return if (thisValue - otherValue == 0) (if (isPass) 1 else 0) - (if (other.isPass) 1 else 0) else thisValue - otherValue
    }

    override fun toString(): String {
        if (isPass) return "PASS"
        val out = StringBuilder(number.toString())
        out.append(suit.symbol)
        for (i in 0 until doubleVal) out.append("X")
        return out.toString()
    }

    private constructor(`in`: Parcel) {
        number = `in`.readInt()
        suit = CardSuit.valueOf(`in`.readString()!!)
        doubleVal = `in`.readInt()
        isPass = `in`.readByte().toInt() != 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(number)
        dest.writeString(suit.name)
        dest.writeInt(doubleVal)
        dest.writeByte((if (isPass) 1 else 0).toByte())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BidObject> = object : Parcelable.Creator<BidObject> {
            override fun createFromParcel(`in`: Parcel): BidObject {
                return BidObject(`in`)
            }

            override fun newArray(size: Int): Array<BidObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}