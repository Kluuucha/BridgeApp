package com.example.bridgeapp.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.structure.BidObject;
import com.example.bridgeapp.structure.ContractObject;

import java.util.ArrayList;
import java.util.List;

public class BidStageState implements Parcelable {

    private final List<BidObject> bidList;
    private int currentPlayer;
    private int lastBidder;
    private ContractObject finalContract;

    public BidStageState(int currentPlayer) {
        this.bidList = new ArrayList<>();
        this.currentPlayer = currentPlayer;
        this.lastBidder = -1;
        this.finalContract = null;
    }

    protected BidStageState(Parcel in) {
        bidList = in.createTypedArrayList(BidObject.CREATOR);
        currentPlayer = in.readInt();
        lastBidder = in.readInt();
        finalContract = in.readTypedObject(ContractObject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bidList);
        dest.writeInt(currentPlayer);
        dest.writeInt(lastBidder);
        dest.writeTypedObject(finalContract, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BidStageState> CREATOR = new Creator<BidStageState>() {
        @Override
        public BidStageState createFromParcel(Parcel in) {
            return new BidStageState(in);
        }

        @Override
        public BidStageState[] newArray(int size) {
            return new BidStageState[size];
        }
    };

    public boolean runBid(int number, String color){
        BidObject bid = new BidObject(number, color);
        if (bidList.isEmpty() || bidList.get(0).compareTo(bid) < 0){
            lastBidder = currentPlayer;
            switchPlayer();
            bidList.add(0, bid);
            return true;
        }
        return false;
    }

    public boolean runDouble(){
        BidObject bid = new BidObject(bidList.get(0), bidList.get(0).getDoubleVal()+1);
        if (bidList.isEmpty() || bidList.get(0).compareTo(bid) <= 0) {
            switchPlayer();
            bidList.add(0, bid);
            return true;
        }
        return false;
    }

    public void runPass(){
        BidObject bid;
        if(bidList.isEmpty())
            bid = new BidObject(0, "NOTRUMP", true);
        else
            bid = new BidObject(bidList.get(0), true);

        switchPlayer();
        bidList.add(0, bid);

        if(passOut() && hasBids())
            finalContract = getContract();
    }

    public ContractObject getContract(){
        if(finalContract != null)
            return finalContract;

        BidObject contractBid = new BidObject(0, "NOTRUMP");
        for(int i = 0; i < 5; i++){
            if (!bidList.get(i).isPass()){
                contractBid = bidList.get(i);
                break;
            }
        }

        return finalContract = new ContractObject(contractBid.getNumber(), contractBid.getSuit(), contractBid.getDoubleVal(), lastBidder);
    }

    public boolean passOut(){ return bidList.size() > 3 && bidList.get(1).isPass() && bidList.get(2).isPass(); }

    public boolean hasBids(){ return lastBidder != -1; }

    public boolean beforeDouble(){ return !bidList.isEmpty() && bidList.get(0).getDoubleVal() == 0; }

    public boolean afterDouble(){ return !bidList.isEmpty() && bidList.get(0).getDoubleVal() > 0; }

    public boolean afterRedouble(){ return !bidList.isEmpty() && bidList.get(0).getDoubleVal() > 1; }

    public void switchPlayer(){
        currentPlayer++;
        fixPlayer();
    }

    public void fixPlayer(){
        if(currentPlayer > 3) currentPlayer %= 4;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getLastBidder() {
        return lastBidder;
    }

    public BidObject getLastBid() {
        return bidList.get(0);
    }

    public List<BidObject> getBidList() {
        return bidList;
    }
}
