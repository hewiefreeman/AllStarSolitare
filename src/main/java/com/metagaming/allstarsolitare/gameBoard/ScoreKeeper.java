package com.metagaming.allstarsolitare.gameBoard;

import android.util.Log;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;

import java.util.ArrayList;
import java.util.List;

public class ScoreKeeper {

    private String TAG = "SCORE_KEEPER";

    private Game game;
    private TextView scoreText;

    //
    List<String> movesList;
    int movesThisTurn;

    //
    int yourScore;
    int timesThroughDeck;

    void init(Game tempGame){
        game = tempGame;
        scoreText = game.mainLayout.findViewById(R.id.game_board_score_text);
        movesList = new ArrayList<>();
        movesThisTurn = 0;
        yourScore = 0;
        timesThroughDeck = 0;
    }

    void checkForUniqueMove(String cardPlacing, String cardTo, Boolean toSuiteStack){
        String moveChecking = cardPlacing+">"+cardTo;
        String moveCheckingReverse = cardTo+">"+cardPlacing;
        if(!movesList.contains(moveChecking) && !movesList.contains(moveCheckingReverse)){
            givePoints(toSuiteStack);
        }
        movesList.add(moveChecking);
    }

    private void givePoints(Boolean toSuiteStack){

        int bonusPoints = 0;

        if(movesThisTurn >= 2){
            bonusPoints = 5+(5*(movesThisTurn-1));
        }

        Log.d(TAG, "BONUS POINTS: "+bonusPoints);
        Log.d(TAG, "MOVES THIS TURN: "+movesThisTurn);

        if(toSuiteStack){
            yourScore += 100+bonusPoints;
            Log.d(TAG, "POINTS GIVEN: "+(100+bonusPoints));
        }else{
            yourScore += 20+bonusPoints;
            Log.d(TAG, "POINTS GIVEN: "+(20+bonusPoints));
        }

        scoreText.setText(String.valueOf(yourScore));

        movesThisTurn++;
    }

    void drawnFromDeck(){
        movesThisTurn = 0;
    }

    void resetDeck(){
        int pointsToRemove = (20+(timesThroughDeck*5))-(5*movesThisTurn);
        if(pointsToRemove > 0){
            yourScore -= pointsToRemove;
        }
        timesThroughDeck++;
        movesThisTurn = 0;
    }
}
