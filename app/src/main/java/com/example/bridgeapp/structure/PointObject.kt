package com.example.bridgeapp.structure

import android.os.Parcel
import android.os.Parcelable

class PointObject : Parcelable {
    val points: Int
    val label: String?

    constructor(points: Int, label: String?) {
        this.points = points
        this.label = label
    }

    private constructor(`in`: Parcel) {
        points = `in`.readInt()
        label = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(points)
        dest.writeString(label)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PointObject?> = object : Parcelable.Creator<PointObject?> {
            override fun createFromParcel(`in`: Parcel): PointObject? {
                return PointObject(`in`)
            }

            override fun newArray(size: Int): Array<PointObject?> {
                return arrayOfNulls(size)
            }
        }

        fun addUp(pointList: List<PointObject?>?, newLabel: String?): PointObject {
            return PointObject(getTotal(pointList), newLabel)
        }

        fun getTotal(pointList: List<PointObject?>?): Int {
            if (pointList!!.isEmpty()) return 0
            var newPoints = 0
            for (p in pointList) newPoints += p!!.points
            return newPoints
        }
    }
}