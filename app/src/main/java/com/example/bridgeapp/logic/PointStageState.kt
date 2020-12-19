package com.example.bridgeapp.logic

import android.os.Parcel
import android.os.Parcelable
import com.example.bridgeapp.structure.ContractObject
import com.example.bridgeapp.structure.HandObject
import com.example.bridgeapp.structure.PointObject
import com.example.bridgeapp.util.CardSuit
import java.util.*

/*
NOTE:   The only score you cannot get from this class is a bonus for winning rubber (2 games).
*/
class PointStageState : Parcelable {
    val pointsBelowLine: MutableList<PointObject>?
    val pointsAboveLine: MutableList<PointObject>?
    val pointsDefence: MutableList<PointObject>?

    constructor() {
        pointsBelowLine = ArrayList()
        pointsAboveLine = ArrayList()
        pointsDefence = ArrayList()
    }

    private constructor(`in`: Parcel) {
        pointsBelowLine = `in`.createTypedArrayList<PointObject>(PointObject.CREATOR)!!.toMutableList()
        pointsAboveLine = `in`.createTypedArrayList<PointObject>(PointObject.CREATOR)!!.toMutableList()
        pointsDefence = `in`.createTypedArrayList<PointObject>(PointObject.CREATOR)!!.toMutableList()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(pointsBelowLine)
        dest.writeTypedList(pointsAboveLine)
        dest.writeTypedList(pointsDefence)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun scoreContract(tricksTaken: Int, contract: ContractObject): Int {
        var points = 0
        if (checkContract(contract, tricksTaken)) {
            if (contract.suit!!.ordinal >= 2) {
                points += 30 * contract.number
                if (contract.suit.ordinal >= 4) points += 10
            } else {
                points += 20 * contract.number
            }
            if (contract.doubleVal > 0) points *= contract.doubleVal * 2
        }
        return points
    }

    fun scoreOvertricks(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean): Int {
        var points = 0
        if (checkContract(contract, tricksTaken)) {
            if (contract.doubleVal == 0) {
                if (contract.suit!!.ordinal >= 2) {
                    points += 30 * (tricksTaken - 6 - contract.number)
                    if (contract.suit.ordinal >= 4) points += 10
                } else {
                    points += 20 * (tricksTaken - 6 - contract.number)
                }
            } else {
                points = 100 * contract.doubleVal * (tricksTaken - 6 - contract.number)
                if (vulnerable) {
                    points *= 2
                }
            }
        }
        return points
    }

    fun scoreMishaps(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean): Int {
        var points = 0
        if (!checkContract(contract, tricksTaken)) {
            if (contract.doubleVal > 0) {
                points += 300
                points *= contract.number + 6 - tricksTaken
                if (!vulnerable) {
                    points -= 300
                }
                points -= 100
                points *= contract.doubleVal
            } else {
                points = 50 * (contract.number + 6 - tricksTaken)
                if (vulnerable) {
                    points *= 2
                }
            }
        }
        return points
    }

    private fun getHonorBonus(hand: HandObject, trump: CardSuit?): Int {
        return if (hand.honorBonus(trump) == 1) 100 else if (hand.honorBonus(trump) == 2) 150 else 0
    }

    private fun getDoubleBonus(doubleValue: Int): Int {
        return 50 * doubleValue
    }

    private fun getSlamBonus(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean): Int {
        if (checkContract(contract, tricksTaken)) {
            var points = 0
            if (contract.number + 6 >= 12) {
                points += 500
                if (vulnerable) points += 250
                if (contract.number + 6 == 13) points *= 2
            }
            return points
        }
        return 0
    }

    private fun getRubberBonus(didWin: Boolean, enemyVulnerable: Boolean): Int {
        return if (didWin) {
            if (enemyVulnerable) 500 else 700
        } else 0
    }

    private fun setPointsBelowLine(tricksTaken: Int, contract: ContractObject) {
        pointsBelowLine!!.add(PointObject(scoreContract(tricksTaken, contract), "Contract"))
    }

    private fun setPointsAboveLine(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean, enemyVulnerable: Boolean, isWinner: Boolean, hands: Array<HandObject>) {
        if (scoreOvertricks(tricksTaken, contract, vulnerable) > 0) pointsAboveLine!!.add(PointObject(scoreOvertricks(tricksTaken, contract, vulnerable), "Overtricks"))
        if (checkContract(contract, tricksTaken) && getDoubleBonus(contract.doubleVal) > 0) pointsAboveLine!!.add(PointObject(getDoubleBonus(contract.doubleVal), if (contract.doubleVal > 1) "Redouble" else "Double"))
        if (checkContract(contract, tricksTaken) && getSlamBonus(tricksTaken, contract, vulnerable) > 0) pointsAboveLine!!.add(PointObject(getSlamBonus(tricksTaken, contract, vulnerable), if (tricksTaken > 12) "Grand slam" else "Small slam"))
        if (getHonorBonus(hands[contract.player], contract.suit) + getHonorBonus(hands[(contract.player + 2) % 4], contract.suit) > 0) pointsAboveLine!!.add(PointObject(getHonorBonus(hands[contract.player], contract.suit) + getHonorBonus(hands[(contract.player + 2) % 4], contract.suit), "Honors"))
        if (getRubberBonus(isWinner, enemyVulnerable) > 0) pointsAboveLine!!.add(PointObject(getRubberBonus(isWinner, enemyVulnerable), "Rubber won"))
    }

    private fun setDefencePoints(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean, enemyVulnerable: Boolean, isWinner: Boolean, hands: Array<HandObject>) {
        if (scoreMishaps(tricksTaken, contract, vulnerable) > 0) pointsDefence!!.add(PointObject(scoreMishaps(tricksTaken, contract, vulnerable), "Mishaps"))
        if (getHonorBonus(hands[(contract.player + 1) % 4], contract.suit) > 0) pointsDefence!!.add(PointObject(getHonorBonus(hands[(contract.player + 1) % 4], contract.suit) + getHonorBonus(hands[(contract.player + 3) % 4], contract.suit), "Honors"))
        if (getRubberBonus(isWinner, enemyVulnerable) > 0) pointsAboveLine!!.add(PointObject(getRubberBonus(isWinner, enemyVulnerable), "Rubber won"))
    }

    fun setPoints(tricksTaken: Int, contract: ContractObject, vulnerable: Boolean, enemyVulnerable: Boolean, winner: Int, hands: Array<HandObject>) {
        pointsDefence!!.clear()
        pointsAboveLine!!.clear()
        pointsBelowLine!!.clear()
        setPointsBelowLine(tricksTaken, contract)
        setPointsAboveLine(tricksTaken, contract, vulnerable, enemyVulnerable, contract.player % 2 == winner, hands)
        setDefencePoints(tricksTaken, contract, vulnerable, vulnerable, (contract.player + 1) % 2 == winner, hands)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PointStageState?> = object : Parcelable.Creator<PointStageState?> {
            override fun createFromParcel(`in`: Parcel): PointStageState? {
                return PointStageState(`in`)
            }

            override fun newArray(size: Int): Array<PointStageState?> {
                return arrayOfNulls(size)
            }
        }

        fun areTricksCorrect(tricks: Int): Boolean {
            return tricks in 0..13
        }

        fun checkContract(contract: ContractObject, tricksTaken: Int): Boolean {
            return tricksTaken - 6 >= contract.number
        }
    }
}