package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.logic.BidStageState
import com.example.bridgeapp.logic.DealStageState
import com.example.bridgeapp.logic.PointStageState
import com.example.bridgeapp.util.GameStage

class PlayObject : Parcelable {
    val dealingPlayer: Int
    var stage: GameStage
    var dealHistory: DealStageState? = null
    var bidHistory: BidStageState? = null
    var pointHistory: PointStageState? = null
    var isNoPointPlay: Boolean
        private set

    constructor(dealingPlayer: Int) {
        this.dealingPlayer = dealingPlayer
        stage = GameStage.DEAL
        isNoPointPlay = false
    }

    val hands: Array<HandObject?>?
        get() = dealHistory!!.hands

    val bids: List<BidObject?>?
        get() = bidHistory!!.bidList

    val contract: ContractObject?
        get() = bidHistory!!.contract

    fun setNoPoint(noPoint: Boolean) {
        isNoPointPlay = noPoint
    }

    constructor(`in`: Parcel) {
        dealingPlayer = `in`.readInt()
        stage = GameStage.valueOf(`in`.readString()!!)
        dealHistory = `in`.readTypedObject(DealStageState.CREATOR)
        bidHistory = `in`.readTypedObject(BidStageState.CREATOR)
        pointHistory = `in`.readTypedObject(PointStageState.CREATOR)
        isNoPointPlay = `in`.readByte().toInt() != 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(dealingPlayer)
        dest.writeString(stage.name)
        dest.writeTypedObject(dealHistory, 0)
        dest.writeTypedObject(bidHistory, 0)
        dest.writeTypedObject(pointHistory, 0)
        dest.writeByte((if (isNoPointPlay) 1 else 0).toByte())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PlayObject?> = object : Parcelable.Creator<PlayObject?> {
            override fun createFromParcel(`in`: Parcel): PlayObject? {
                return PlayObject(`in`)
            }

            override fun newArray(size: Int): Array<PlayObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}