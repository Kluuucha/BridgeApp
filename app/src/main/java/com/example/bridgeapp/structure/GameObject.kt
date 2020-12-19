package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class GameObject : Parcelable {
    val plays: MutableList<PlayObject>?
    var isFinished: Boolean
        private set

    constructor(player: Int) {
        plays = ArrayList()
        isFinished = false
        startNewPlay(player)
    }

    private constructor(`in`: Parcel) {
        plays = `in`.createTypedArrayList<PlayObject>(PlayObject.CREATOR)!!.toMutableList()
        isFinished = `in`.readByte().toInt() != 0
    }

    val latestPlay: PlayObject
        get() = plays!![plays.size - 1]

    private fun startNewPlay(player: Int) {
        plays!!.add(PlayObject(player % 4))
    }

    fun startNewPlay() {
        if (plays!!.isEmpty()) startNewPlay(0) else startNewPlay((latestPlay.dealingPlayer + 1) % 4)
    }

    val winner: Int
        get() {
            val scores = IntArray(2)
            for (play in plays!!) {
                if (!play.isNoPointPlay) {
                    val contractedTeam = play.contract!!.player % 2
                    scores[contractedTeam] = PointObject.getTotal(play.pointHistory!!.pointsBelowLine)
                    for (i in scores.indices) {
                        if (scores[i] >= 100) {
                            isFinished = true
                            return i
                        }
                    }
                }
            }
            return -1
        }

    val points: IntArray
        get() {
            val scores = intArrayOf(0, 0)
            for (play in plays!!) {
                if (!play.isNoPointPlay) {
                    val contractedTeam = play.contract!!.player % 2
                    scores[contractedTeam] += PointObject.getTotal(play.pointHistory!!.pointsBelowLine)
                    scores[contractedTeam] += PointObject.getTotal(play.pointHistory!!.pointsAboveLine)
                    scores[(contractedTeam + 1) % 2] += PointObject.getTotal(play.pointHistory!!.pointsDefence)
                }
            }
            return scores
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(plays)
        dest.writeByte((if (isFinished) 1 else 0).toByte())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GameObject?> = object : Parcelable.Creator<GameObject?> {
            override fun createFromParcel(`in`: Parcel): GameObject? {
                return GameObject(`in`)
            }

            override fun newArray(size: Int): Array<GameObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}