package com.example.bridgeapp.logic

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.structure.BidObject
import com.example.bridgeapp.structure.ContractObject
import java.util.*
import java.io.Serializable

class BidStageState : Parcelable, Serializable {
    val bidList: MutableList<BidObject>?
    var currentPlayer: Int
        private set
    var lastBidder: Int
        private set
    private var finalContract: ContractObject?

    constructor(currentPlayer: Int) {
        bidList = ArrayList()
        this.currentPlayer = currentPlayer
        lastBidder = -1
        finalContract = null
    }

    private constructor(`in`: Parcel) {
        bidList = `in`.createTypedArrayList<BidObject>(BidObject.CREATOR)!!.toMutableList()
        currentPlayer = `in`.readInt()
        lastBidder = `in`.readInt()
        finalContract = `in`.readTypedObject(ContractObject.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(bidList)
        dest.writeInt(currentPlayer)
        dest.writeInt(lastBidder)
        dest.writeTypedObject(finalContract, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun runBid(number: Int, color: String): Boolean {
        val bid = BidObject(number, color)
        if (bidList!!.isEmpty() || bidList[0] < bid) {
            lastBidder = currentPlayer
            switchPlayer()
            bidList.add(0, bid)
            return true
        }
        return false
    }

    fun runDouble(): Boolean {
        val bid = BidObject(bidList!![0], bidList[0].doubleVal + 1)
        if (bidList.isEmpty() || bidList[0] <= bid) {
            switchPlayer()
            bidList.add(0, bid)
            return true
        }
        return false
    }

    fun runPass() {
        val bid: BidObject = if (bidList!!.isEmpty()) BidObject(0, "NOTRUMP", true) else BidObject(bidList[0], true)
        switchPlayer()
        bidList.add(0, bid)
        if (passOut() && hasBids()) finalContract = contract
    }

    val contract: ContractObject
        get() {
            if (finalContract != null) return finalContract as ContractObject
            var contractBid = BidObject(0, "NOTRUMP")
            for (i in 0..4) {
                if (!bidList!![i].isPass) {
                    contractBid = bidList[i]
                    break
                }
            }
            return ContractObject(contractBid.number, contractBid.suit, contractBid.doubleVal, lastBidder).also { finalContract = it }
        }

    fun passOut(): Boolean {
        return bidList!!.size > 3 && bidList[1].isPass && bidList[2].isPass
    }

    fun hasBids(): Boolean {
        return lastBidder != -1
    }

    fun beforeDouble(): Boolean {
        return bidList!!.isNotEmpty() && bidList[0].doubleVal == 0
    }

    fun afterDouble(): Boolean {
        return bidList!!.isNotEmpty() && bidList[0].doubleVal > 0
    }

    fun afterRedouble(): Boolean {
        return bidList!!.isNotEmpty() && bidList[0].doubleVal > 1
    }

    private fun switchPlayer() {
        currentPlayer++
        fixPlayer()
    }

    private fun fixPlayer() {
        if (currentPlayer > 3) currentPlayer %= 4
    }

    val lastBid: BidObject
        get() = bidList!![0]

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BidStageState?> = object : Parcelable.Creator<BidStageState?> {
            override fun createFromParcel(`in`: Parcel): BidStageState? {
                return BidStageState(`in`)
            }

            override fun newArray(size: Int): Array<BidStageState?> {
                return arrayOfNulls(size)
            }
        }
    }
}