package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.util.CardSuit;

public class ContractObject implements Parcelable {

    private final int number;
    private final CardSuit suit;
    private final int doubleVal;
    private final int player;

    public ContractObject(int number, CardSuit suit, int doubleVal, int player) {
        this.number = number;
        this.suit = suit;
        this.doubleVal = doubleVal;
        this.player = player;
    }

    public ContractObject() {
        this(0,CardSuit.CLUBS,0,0);
    }

    public static final Creator<ContractObject> CREATOR = new Creator<ContractObject>() {
        @Override
        public ContractObject createFromParcel(Parcel in) {
            return new ContractObject(in);
        }

        @Override
        public ContractObject[] newArray(int size) {
            return new ContractObject[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public int getPlayer() {
        return player;
    }

    public int getDoubleVal() {
        return doubleVal;
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder(String.valueOf(number));
        out.append(suit.symbol);
        for(int i = 0; i < doubleVal; i++)
            out.append("X");

        return out.toString();
    }

    protected ContractObject(Parcel in) {
        number = in.readInt();
        suit = CardSuit.valueOf(in.readString());
        doubleVal = in.readInt();
        player = in.readInt();
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
        dest.writeInt(player);
    }
}
