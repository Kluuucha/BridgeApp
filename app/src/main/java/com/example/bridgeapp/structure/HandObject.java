package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.util.CardSuit;
import com.example.bridgeapp.util.CardValue;

public class HandObject implements Parcelable {

    private final SuitObject[] suits;

    public HandObject() {
        suits = new SuitObject[CardSuit.values().length - 1];

        for(int i = 0; i < suits.length; i++){
            suits[i] = new SuitObject(CardSuit.values()[i]);
        }
    }

    public static final Creator<HandObject> CREATOR = new Creator<HandObject>() {
        @Override
        public HandObject createFromParcel(Parcel in) {
            return new HandObject(in);
        }

        @Override
        public HandObject[] newArray(int size) {
            return new HandObject[size];
        }
    };

    public SuitObject[] getSuits() {
        return suits;
    }

    public void addCard (CardSuit suit, CardValue value){
        if(!hasCard(suit, value)) {
            suits[suit.ordinal()].addCard(value);
        }
    }

    public boolean hasCard (CardSuit suit, CardValue value){
        return suits[suit.ordinal()].hasCard(value);
    }

    public int honorBonus (CardSuit trump){
        if(trump == CardSuit.NOTRUMP){
            for(SuitObject s: suits){
                if (!s.hasCard(CardValue.ACE))
                    return 0;
            }
            return 2;
        }
        else{
            int trumps = 0;
            for (CardValue c : suits[trump.ordinal()].getCards()){
                if (c !=null && c.ordinal() >= 8)
                    trumps++;
            }
            if (trumps - 3 > 0) return trumps - 3;
        }
        return 0;
    }

    public static HandObject fromStringArray(String[] array){
        HandObject hand = new HandObject();
        for(String s: array){
            String[]card = s.split("_");
            hand.addCard(CardSuit.valueOf(card[1]), CardValue.valueOf(card[0]));
        }
        return hand;
    }

//Parcelable implementation

    protected HandObject(Parcel in) {
        suits = in.createTypedArray(SuitObject.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(suits, flags);
    }
}
