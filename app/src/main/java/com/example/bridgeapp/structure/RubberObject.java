package com.example.bridgeapp.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RubberObject implements Parcelable {

    private final List<GameObject> games;
    private String[] playerNames = new String[4];
    private boolean finished;

    public RubberObject() {
        this(0);
    }

    public RubberObject(String[] playerNames) {
        this(0, playerNames);
    }

    public RubberObject(int player) {
        this.games = new ArrayList<>();
        this.finished = false;
        startNewGame(player);
    }

    public RubberObject(int player, String[] playerNames) {
        this.games = new ArrayList<>();
        this.finished = false;
        startNewGame(player);
        this.playerNames = playerNames;
    }

    protected RubberObject(Parcel in) {
        games = in.createTypedArrayList(GameObject.CREATOR);
        playerNames = in.createStringArray();
        finished = in.readByte() != 0;
    }

    public static final Creator<RubberObject> CREATOR = new Creator<RubberObject>() {
        @Override
        public RubberObject createFromParcel(Parcel in) {
            return new RubberObject(in);
        }

        @Override
        public RubberObject[] newArray(int size) {
            return new RubberObject[size];
        }
    };

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public boolean getVulnerability(int team){
        for(GameObject g : games){
            if (g.isFinished() && g.getWinner() == team)
                return true;
        }
        return false;
    }

    public GameObject getLatestGame(){
        return games.get(games.size()-1);
    }

    public void startNewGame(int player){
        games.add(new GameObject(player));
    }

    public void startNewGame(){
        if(games.isEmpty())
            startNewGame(0);
        else
            startNewGame((getLatestGame().getLatestPlay().getDealingPlayer()+1)%4);
    }

    public List<GameObject> getGames() {
        return games;
    }

    public boolean isFinished(){
        if(finished) return true;

        int[] wins = new int[2];

        for(GameObject g : games){
            if (g.isFinished()){
                wins[g.getWinner()]++;
            }
            for(int i: wins){
                if(i>=2){
                    finished = true;
                    return true;
                }
            }
        }
        return false;
    }

    public int[] getScores(){
        if(!finished) return null;

        int[] scores = {0, 0};

        for(GameObject g : games){
            int[] gameScore = g.getPoints();
            for(int i = 0; i < gameScore.length; i++){
                scores[i] += gameScore[i];
            }
        }

        return scores;
    }

    public int getWinner(){
        if(!finished) return -1;
        int[] scores = getScores();
        if(scores[0] > scores[1]) return 0;
        else if (scores[0] < scores[1]) return 1;
        else return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(games);
        dest.writeStringArray(playerNames);
        dest.writeByte((byte) (finished ? 1 : 0));
    }
}
