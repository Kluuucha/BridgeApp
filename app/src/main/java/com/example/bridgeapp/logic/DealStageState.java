package com.example.bridgeapp.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.structure.HandObject;
import com.example.bridgeapp.structure.SuitObject;
import com.example.bridgeapp.util.CardValue;

import java.util.ArrayList;
import java.util.List;

public class DealStageState implements Parcelable {

    private final HandObject[] hands;
    private List<String> cards = new ArrayList<>();

    private int currentPlayer;
    private boolean finished;

    public DealStageState(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        hands = new HandObject[4];
        finished = false;
    }

    public static final Creator<DealStageState> CREATOR = new Creator<DealStageState>() {
        @Override
        public DealStageState createFromParcel(Parcel in) {
            return new DealStageState(in);
        }

        @Override
        public DealStageState[] newArray(int size) {
            return new DealStageState[size];
        }
    };

    public void setHandForCurrentPlayer(HandObject hand){
        setHandForPlayer(hand, currentPlayer);
    }

    public void setHandForPlayer(HandObject hand, int player){
        hands[player] = hand;
    }

    public boolean playerHandCorrect(int player){
        for (int i = 1; i<3; i++){
            int checked = (i+player)%4;
            for(SuitObject s : hands[player].getSuits()){
                for(CardValue c: s.getCards()){
                    if(c != null && hands[checked].hasCard(s.getSuit(), c))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean checkHands(){
        for (int i = 0; i<3; i++) {
            if(!playerHandCorrect(i))
                return false;
        }
        finished = true;
        return true;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer(){
        currentPlayer = (currentPlayer+1)%4;
    }

    public HandObject[] getHands() {
        return hands;
    }

    public List<String> getCardList() {
        return cards;
    }

    public void addCard(String card){
        cards.add(card);
    }

    public void removeCard(String card){
        cards.remove(card);
    }

    public void clearCardList(){
        cards.clear();
    }

    public int cardCount(){
        return cards.size();
    }

    public boolean isFinished() {
        checkHands();
        return finished;
    }

//Parcelable implementation

    protected DealStageState(Parcel in) {
        hands = in.createTypedArray(HandObject.CREATOR);
        cards = in.createStringArrayList();
        currentPlayer = in.readInt();
        finished = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(hands, flags);
        dest.writeStringList(cards);
        dest.writeInt(currentPlayer);
        dest.writeByte((byte) (finished ? 1 : 0));
    }
}
