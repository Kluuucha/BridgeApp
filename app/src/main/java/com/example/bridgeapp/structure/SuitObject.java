package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.util.CardSuit;
import com.example.bridgeapp.util.CardValue;

import java.util.Arrays;

public class SuitObject implements Parcelable {
    private final CardSuit suit;
    private final CardValue[] cards;

    public static final Creator<SuitObject> CREATOR = new Creator<SuitObject>() {
        @Override
        public SuitObject createFromParcel(Parcel in) {
            return new SuitObject(in);
        }

        @Override
        public SuitObject[] newArray(int size) {
            return new SuitObject[size];
        }
    };

    public SuitObject(CardSuit suit) {
        this.suit = suit;
        cards = new CardValue[CardValue.values().length];
    }

    public void addCard(CardValue card){
        if(!hasCard(card)){
            cards[card.ordinal()] = card;
        }
    }

    public boolean hasCard(CardValue card){
        return Arrays.asList(cards).contains(card);
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardValue[] getCards() {
        return cards;
    }

//Parcelable implementation

    protected SuitObject(Parcel in) {
        this.suit = CardSuit.valueOf(in.readString());
        String[] cardNames = in.createStringArray();

        cards = new CardValue[cardNames.length];

        for(int i = 0; i < cardNames.length; i++) {
            if (cardNames[i] != null)
                cards[i] = CardValue.valueOf(cardNames[i]);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] cardNames = new String[cards.length];
        for(int i = 0; i < cardNames.length; i++) {
            if(cards[i] != null)
                cardNames[i] = cards[i].name();
        }

        dest.writeString(this.suit.name());
        dest.writeStringArray(cardNames);
    }
}
