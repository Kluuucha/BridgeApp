package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable
import java.io.*
import java.util.*


class RubberObject : Parcelable, Serializable {
    val games: MutableList<GameObject>?
    lateinit var playerNames: Array<String>
    private var finished: Boolean

    constructor(playerNames: Array<String>) : this(0, playerNames)

    @JvmOverloads
    constructor(player: Int = 0) {
        games = ArrayList()
        finished = false
        startNewGame(player)
    }

    constructor(player: Int, playerNames: Array<String>) {
        games = ArrayList()
        finished = false
        startNewGame(player)
        this.playerNames = playerNames
    }

    private constructor(`in`: Parcel) {
        games = `in`.createTypedArrayList<GameObject>(GameObject.CREATOR)!!.toMutableList()
        playerNames = `in`.createStringArray() as Array<String>
        finished = `in`.readByte().toInt() != 0
    }

    fun getVulnerability(team: Int): Boolean {
        for (g in games!!) {
            if (g.isFinished && g.winner == team) return true
        }
        return false
    }

    val latestGame: GameObject
        get() = games!![games.size - 1]

    private fun startNewGame(player: Int) {
        games!!.add(GameObject(player))
    }

    fun startNewGame() {
        if (games!!.isEmpty()) startNewGame(0) else startNewGame((latestGame.latestPlay.dealingPlayer + 1) % 4)
    }

    fun isFinished(): Boolean {
        if (finished) return true
        val wins = IntArray(2)
        for (g in games!!) {
            if (g.isFinished) {
                wins[g.winner]++
            }
            for (i in wins) {
                if (i >= 2) {
                    finished = true
                    return true
                }
            }
        }
        return false
    }

    val scores: IntArray?
        get() {
            if (!finished) return null
            val scores = intArrayOf(0, 0)
            for (g in games!!) {
                val gameScore = g.points
                for (i in gameScore.indices) {
                    scores[i] += gameScore[i]
                }
            }
            return scores
        }

    val winner: Int
        get() {
            if (!finished) return -1
            val scores = scores
            return if (scores!![0] > scores[1]) 0 else if (scores[0] < scores[1]) 1 else -1
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(games)
        dest.writeStringArray(playerNames)
        dest.writeByte((if (finished) 1 else 0).toByte())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RubberObject?> = object : Parcelable.Creator<RubberObject?> {
            override fun createFromParcel(`in`: Parcel): RubberObject? {
                return RubberObject(`in`)
            }

            override fun newArray(size: Int): Array<RubberObject?> {
                return arrayOfNulls(size)
            }
        }
    }
}