package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.logic.BidStageState;
import com.example.bridgeapp.logic.DealStageState;
import com.example.bridgeapp.logic.PointStageState;
import com.example.bridgeapp.util.GameStage;

import java.util.List;

public class PlayObject implements Parcelable {

    private final int dealingPlayer;

    private GameStage stage;
    private DealStageState dealHistory;
    private BidStageState bidHistory;
    private PointStageState pointHistory;
    private boolean noPointPlay;

    public PlayObject(int dealingPlayer) {
        this.dealingPlayer = dealingPlayer;
        stage = GameStage.DEAL;
        noPointPlay = false;
    }

    public static final Creator<PlayObject> CREATOR = new Creator<PlayObject>() {
        @Override
        public PlayObject createFromParcel(Parcel in) {
            return new PlayObject(in);
        }

        @Override
        public PlayObject[] newArray(int size) {
            return new PlayObject[size];
        }
    };

    public void setStage(GameStage stage) {
        this.stage = stage;
    }

    public GameStage getStage() {
        return stage;
    }

    public void setDealHistory(DealStageState dealHistory) {
        this.dealHistory = dealHistory;
    }

    public void setBidHistory(BidStageState bidHistory){
        this.bidHistory = bidHistory;
    }

    public void setPointHistory(PointStageState pointHistory){
        this.pointHistory = pointHistory;
    }

    public DealStageState getDealHistory() {
        return dealHistory;
    }

    public BidStageState getBidHistory() {
        return bidHistory;
    }

    public PointStageState getPointHistory() {
        return pointHistory;
    }

    public HandObject[] getHands() {
        return this.dealHistory.getHands();
    }

    public List<BidObject> getBids(){
        return this.bidHistory.getBidList();
    }

    public ContractObject getContract() {
        return this.bidHistory.getContract();
    }

    public int getDealingPlayer() {
        return dealingPlayer;
    }

    public void setNoPoint(boolean noPoint) {
        this.noPointPlay = noPoint;
    }

    public boolean isNoPointPlay() {
        return noPointPlay;
    }

    public PlayObject(Parcel in) {
        this.dealingPlayer = in.readInt();
        this.stage = GameStage.valueOf(in.readString());
        this.dealHistory = in.readTypedObject(DealStageState.CREATOR);
        this.bidHistory = in.readTypedObject(BidStageState.CREATOR);
        this.pointHistory = in.readTypedObject(PointStageState.CREATOR);
        this.noPointPlay = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dealingPlayer);
        dest.writeString(this.stage.name());
        dest.writeTypedObject(this.dealHistory, 0);
        dest.writeTypedObject(this.bidHistory, 0);
        dest.writeTypedObject(this.pointHistory, 0);
        dest.writeByte((byte) (noPointPlay ? 1 : 0));
    }
}
