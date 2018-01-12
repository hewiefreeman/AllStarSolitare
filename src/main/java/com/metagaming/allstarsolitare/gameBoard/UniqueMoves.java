package com.metagaming.allstarsolitare.gameBoard;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class UniqueMoves {

    List<Object[]> uniqueMovesList;
    // Object[fromTo, bonusType, bonusParams, points, turnNumb]
    // 0 (string) - card names where "from" is the moving card, and "to" is the one it's being placed on
    // 1 (string) - the type of bonus the move might have. Blank means no move bonus
    // 2 (string) - any parameters the move's bonus might need
    // 3 (int)    - the amount of pints made by the move. Set by ScoreKeeper.givePoints

    void init(){
        uniqueMovesList = new ArrayList<>();
    }

    void addMove(String fromTo, String bonusType, String bonusParams){
        uniqueMovesList.add(new Object[]{fromTo, bonusType, bonusParams, 0});
    }

    int getMoveIndex(String theMove){
        for(int i = 0; i < uniqueMovesList.size(); i++){
            if(uniqueMovesList.get(i)[0].equals(theMove)){
                return i;
            }
        }
        return -1;
    }

    int getPoints(String theMove){
        for(int i = 0; i < uniqueMovesList.size(); i++){
            if(uniqueMovesList.get(i)[0].equals(theMove)){
                return (int) uniqueMovesList.get(i)[3];
            }
        }
        return -1;
    }

    Object[] getLastMove(){
        if(uniqueMovesList.size()-2 >= 0){
            return uniqueMovesList.get(uniqueMovesList.size()-2);
        }else{
            return null;
        }
    }

    Boolean containsMove(String theMove){
        for(int i = 0; i < uniqueMovesList.size(); i++){
            if(uniqueMovesList.get(i)[0].equals(theMove)){
                return true;
            }
        }
        //
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////
    //  SPECIAL MOVES   /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    // AN ACE MANEUVER IS WHEN YOU SPLIT A FIELD STACK TO UNCOVER A CARD TO PUT
    // IN AN ACE PILE.
    Boolean checkForSplitManeuverSuite(String cardPlacing) {
        return getLastMove() != null && (getLastMove()[1].equals("splitManeuver") && getLastMove()[2].equals(cardPlacing));
    }
}