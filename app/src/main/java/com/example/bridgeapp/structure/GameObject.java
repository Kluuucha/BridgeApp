package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Parcelable {

    private final List<PlayObject> plays;
    private boolean finished;

    public GameObject(int player) {
        this.plays = new ArrayList<>();
        this.finished = false;
        startNewPlay(player);
    }

    protected GameObject(Parcel in) {
        plays = in.createTypedArrayList(PlayObject.CREATOR);
        finished = in.readByte() != 0;
    }

    public static final Creator<GameObject> CREATOR = new Creator<GameObject>() {
        @Override
        public GameObject createFromParcel(Parcel in) {
            return new GameObject(in);
        }

        @Override
        public GameObject[] newArray(int size) {
            return new GameObject[size];
        }
    };

    public PlayObject getLatestPlay(){
        return plays.get(plays.size()-1);
    }

    public void startNewPlay(int player){
        plays.add(new PlayObject(player%4));
    }

    public void startNewPlay(){
        if(plays.isEmpty())
            startNewPlay(0);
        else
            startNewPlay((getLatestPlay().getDealingPlayer()+1)%4);
    }

    public List<PlayObject> getPlays() {
        return plays;
    }

    public int getWinner(){
        int[] scores = new int[2];
        for(PlayObject play : plays){
            if(!play.isNoPointPlay())
            {
                int contractedTeam = play.getContract().getPlayer()%2;
                scores[contractedTeam] = PointObject.getTotal(play.getPointHistory().getPointsBelowLine());
                for(int i= 0; i < scores.length; i++){
                    if(scores[i] >= 100) {
                        finished = true;
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public boolean isFinished() {
        return finished;
    }

    public int[] getPoints(){
        int[] scores = {0, 0};

        for(PlayObject play : plays){
            if(!play.isNoPointPlay()) {
                int contractedTeam = play.getContract().getPlayer() % 2;
                scores[contractedTeam] += PointObject.getTotal(play.getPointHistory().getPointsBelowLine());
                scores[contractedTeam] += PointObject.getTotal(play.getPointHistory().getPointsAboveLine());
                scores[(contractedTeam + 1) % 2] += PointObject.getTotal(play.getPointHistory().getPointsDefence());
            }
        }

        return scores;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(plays);
        dest.writeByte((byte) (finished ? 1 : 0));
    }
}
