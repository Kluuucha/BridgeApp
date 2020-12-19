package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PointObject implements Parcelable {

    private final int points;
    private final String label;

    public PointObject(int points, String label) {
        this.points = points;
        this.label = label;
    }

    protected PointObject(Parcel in) {
        points = in.readInt();
        label = in.readString();
    }

    public static final Creator<PointObject> CREATOR = new Creator<PointObject>() {
        @Override
        public PointObject createFromParcel(Parcel in) {
            return new PointObject(in);
        }

        @Override
        public PointObject[] newArray(int size) {
            return new PointObject[size];
        }
    };

    public int getPoints() {
        return points;
    }

    public String getLabel() {
        return label;
    }

    public static PointObject addUp(List<PointObject> pointList, String newLabel){
        return new PointObject(getTotal(pointList), newLabel);
    }

    public static int getTotal(List<PointObject> pointList){
        if(pointList.isEmpty()) return 0;
        int newPoints = 0;
        for(PointObject p: pointList)
            newPoints += p.getPoints();
        return newPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(points);
        dest.writeString(label);
    }
}
