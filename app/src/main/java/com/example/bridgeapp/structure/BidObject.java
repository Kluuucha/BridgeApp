package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.util.CardSuit;

public class BidObject implements Comparable<BidObject>, Parcelable {

    private final int number;
    private final CardSuit suit;
    private final int doubleVal;
    private final boolean pass;

    public BidObject(int number, String suit) {
        this(number, suit, false);
    }

    public BidObject(int number, String suit, boolean pass) {
        this.number = number;

        System.out.println(suit.trim());
        this.suit = CardSuit.valueOf(suit.trim());

        doubleVal = 0;
        this.pass = pass;
    }

    public BidObject(BidObject o, int doubleVal, boolean pass){
        this.number = o.number;
        this.suit = o.suit;
        this.doubleVal = doubleVal;
        this.pass = pass;
    }

    public BidObject(BidObject o, int doubleVal){
        this(o, doubleVal, false);
    }

    public BidObject(BidObject o, boolean pass){
        this(o, 0, pass);
    }

    public static final Creator<BidObject> CREATOR = new Creator<BidObject>() {
        @Override
        public BidObject createFromParcel(Parcel in) {
            return new BidObject(in);
        }

        @Override
        public BidObject[] newArray(int size) {
            return new BidObject[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public int getDoubleVal() {
        return doubleVal;
    }

    public boolean isPass() {
        return pass;
    }

    public int getValue(){
        return this.number * 100 + this.suit.ordinal() * 10 + this.doubleVal;
    }

    @Override
    public int compareTo(BidObject o){
        int thisValue = this.getValue();
        int otherValue = o.getValue();

        if(thisValue - otherValue == 0)
            return (this.pass ? 1 : 0) - ( o.pass ? 1 : 0);
        else
            return thisValue - otherValue;
    }

    @Override
    public String toString(){
        if(pass) return "PASS";

        StringBuilder out = new StringBuilder(String.valueOf(number));
        out.append(suit.symbol);
        for(int i = 0; i < doubleVal; i++)
            out.append("X");

        return out.toString();
    }

    protected BidObject(Parcel in) {
        number = in.readInt();
        suit = CardSuit.valueOf(in.readString());
        doubleVal = in.readInt();
        pass = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(this.suit.name());
        dest.writeInt(doubleVal);
        dest.writeByte((byte) (pass ? 1 : 0));
    }
}
