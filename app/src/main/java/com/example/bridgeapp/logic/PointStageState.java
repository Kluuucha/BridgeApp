package com.example.bridgeapp.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bridgeapp.structure.ContractObject;
import com.example.bridgeapp.structure.HandObject;
import com.example.bridgeapp.structure.PointObject;
import com.example.bridgeapp.util.CardSuit;

import java.util.ArrayList;
import java.util.List;

/*
NOTE:   The only score you cannot get from this class is a bonus for winning rubber (2 games).
*/

public class PointStageState implements Parcelable {

    private final List<PointObject> pointsBelowLine;
    private final List<PointObject> pointsAboveLine;

    private final List<PointObject> pointsDefence;

    public PointStageState() {
        pointsBelowLine = new ArrayList<>();
        pointsAboveLine = new ArrayList<>();

        pointsDefence = new ArrayList<>();
    }

    protected PointStageState(Parcel in) {
        pointsBelowLine = in.createTypedArrayList(PointObject.CREATOR);
        pointsAboveLine = in.createTypedArrayList(PointObject.CREATOR);
        pointsDefence = in.createTypedArrayList(PointObject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pointsBelowLine);
        dest.writeTypedList(pointsAboveLine);
        dest.writeTypedList(pointsDefence);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PointStageState> CREATOR = new Creator<PointStageState>() {
        @Override
        public PointStageState createFromParcel(Parcel in) {
            return new PointStageState(in);
        }

        @Override
        public PointStageState[] newArray(int size) {
            return new PointStageState[size];
        }
    };

    public int scoreContract(int tricksTaken, ContractObject contract) {
        int points = 0;
        if (checkContract(contract, tricksTaken)) {
            if(contract.getSuit().ordinal() >=2){
                points += 30 * contract.getNumber();
                if(contract.getSuit().ordinal() >=4)
                    points += 10;
            }
            else {
                points += 20 * contract.getNumber();
            }
            if(contract.getDoubleVal() > 0)
                points *= contract.getDoubleVal() * 2;
        }
        return points;
    }

    public int scoreOvertricks(int tricksTaken, ContractObject contract, boolean vulnerable){
        int points = 0;
        if (checkContract(contract, tricksTaken)) {
            if(contract.getDoubleVal() == 0){
                if(contract.getSuit().ordinal() >=2){
                points += 30 * ((tricksTaken - 6) - contract.getNumber());
                if(contract.getSuit().ordinal() >=4)
                    points += 10;
                }
                else {
                    points += 20 * ((tricksTaken - 6) - contract.getNumber());
                }
            }
            else{
                points = 100 * contract.getDoubleVal() * ((tricksTaken - 6) - contract.getNumber());
                if(vulnerable){
                    points *= 2;
                }
            }
        }
        return points;
    }

    public int scoreMishaps(int tricksTaken, ContractObject contract, boolean vulnerable){
        int points = 0;
        if (!checkContract(contract, tricksTaken)) {
            if(contract.getDoubleVal() > 0){
                points += 300;
                points *= (contract.getNumber() + 6) - tricksTaken;
                if(!vulnerable){
                    points -= 300;
                }
                points -= 100;
                points *= contract.getDoubleVal();
            }
            else{
                points = 50 * ((contract.getNumber() + 6) - tricksTaken);
                if(vulnerable){
                    points *= 2;
                }
            }
        }
        return points;
    }


    public int getHonorBonus(HandObject hand, CardSuit trump){
        if (hand.honorBonus(trump)==1) return 100;
        else if (hand.honorBonus(trump)==2) return 150;
        else return 0;
    }

    public int getDoubleBonus(int doubleValue){
        return 50 * doubleValue;
    }

    public int getSlamBonus(int tricksTaken, ContractObject contract, boolean vulnerable){
        if(checkContract(contract, tricksTaken)){
            int points = 0;
            if(contract.getNumber()+6 >=12){
                points += 500;
                if(vulnerable)
                    points += 250;
                if(contract.getNumber()+6 == 13)
                    points *= 2;
            }
            return points;
        }
        return 0;
    }

    public int getRubberBonus(boolean didWin, boolean enemyVulnerable){
        if(didWin){
            if(enemyVulnerable)
                return 500;
            else
                return 700;
        }
        return 0;
    }

    public static boolean areTricksCorrect(int tricks){
        return (tricks >= 0 && tricks <= 13);
    }

    public static boolean checkContract(ContractObject contract, int tricksTaken){
        return tricksTaken - 6 >= contract.getNumber();
    }

    private void setPointsBelowLine(int tricksTaken, ContractObject contract){
        pointsBelowLine.add(new PointObject(scoreContract(tricksTaken, contract), "Contract"));
    }

    private void setPointsAboveLine(int tricksTaken, ContractObject contract, boolean vulnerable, boolean enemyVulnerable, boolean isWinner, HandObject[] hands){
        if(scoreOvertricks(tricksTaken, contract, vulnerable) > 0)
            pointsAboveLine.add(new PointObject(scoreOvertricks(tricksTaken, contract, vulnerable), "Overtricks"));
        if(checkContract(contract, tricksTaken) && getDoubleBonus(contract.getDoubleVal()) > 0)
            pointsAboveLine.add(new PointObject(getDoubleBonus(contract.getDoubleVal()),(contract.getDoubleVal()>1 ? "Redouble": "Double")));
        if(checkContract(contract, tricksTaken) && getSlamBonus(tricksTaken, contract, vulnerable) > 0)
            pointsAboveLine.add(new PointObject(getSlamBonus(tricksTaken, contract, vulnerable),(tricksTaken > 12 ? "Grand slam":"Small slam")));
        if(getHonorBonus(hands[contract.getPlayer()], contract.getSuit()) + getHonorBonus(hands[(contract.getPlayer()+2)%4], contract.getSuit()) > 0)
            pointsAboveLine.add(new PointObject(getHonorBonus(hands[contract.getPlayer()], contract.getSuit()) + getHonorBonus(hands[(contract.getPlayer()+2)%4], contract.getSuit()), "Honors"));
        if(getRubberBonus(isWinner, enemyVulnerable) > 0)
              pointsAboveLine.add(new PointObject(getRubberBonus(isWinner, enemyVulnerable), "Rubber won"));
    }

    private void setDefencePoints(int tricksTaken, ContractObject contract, boolean vulnerable, boolean enemyVulnerable, boolean isWinner, HandObject[] hands){
        if(scoreMishaps(tricksTaken, contract, vulnerable) > 0)
            pointsDefence.add(new PointObject(scoreMishaps(tricksTaken, contract, vulnerable), "Mishaps"));
        if(getHonorBonus(hands[(contract.getPlayer()+1)%4], contract.getSuit()) > 0)
            pointsDefence.add(new PointObject(getHonorBonus(hands[(contract.getPlayer()+1)%4], contract.getSuit()) + getHonorBonus(hands[(contract.getPlayer()+3)%4], contract.getSuit()), "Honors"));
        if(getRubberBonus(isWinner, enemyVulnerable) > 0)
            pointsAboveLine.add(new PointObject(getRubberBonus(isWinner, enemyVulnerable), "Rubber won"));
    }

    public void setPoints(int tricksTaken, ContractObject contract, boolean vulnerable, boolean enemyVulnerable, int winner, HandObject[] hands){
        pointsDefence.clear();
        pointsAboveLine.clear();
        pointsBelowLine.clear();

        setPointsBelowLine(tricksTaken, contract);
        setPointsAboveLine(tricksTaken, contract, vulnerable, enemyVulnerable, contract.getPlayer()%2 == winner, hands);
        setDefencePoints(tricksTaken, contract, vulnerable, vulnerable, (contract.getPlayer()+1)%2 == winner, hands);
    }

    public List<PointObject> getPointsBelowLine() {
        return pointsBelowLine;
    }

    public List<PointObject> getPointsAboveLine() {
        return pointsAboveLine;
    }

    public List<PointObject> getPointsDefence() {
        return pointsDefence;
    }

}
