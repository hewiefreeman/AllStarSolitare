package com.metagaming.allstarsolitare.gameBoard;

import android.util.Log;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;

import java.util.ArrayList;
import java.util.List;

class ScoreKeeper {

    private String TAG = "SCORE_KEEPER";
    private Game game;

    TextView scoreText;

    //
    List<String> movesList;
    int movesThisTurn;

    //
    int yourScore;
    int timesThroughDeck;

    //
    private final int points_unique_move = 50;
    private final int points_ace_pile = 200;
    private final int points_combo_base = 15;
    private final int points_combo_increment = 10;
    private final int points_leet_switch = 175;

    void init(Game tempGame){
        game = tempGame;
        scoreText = game.mainLayout.findViewById(R.id.game_board_score_text);
        movesList = new ArrayList<>();
        movesThisTurn = 0;
        yourScore = 0;
        timesThroughDeck = 0;
    }

    void checkForUniqueMove(String cardPlacing, String cardTo, Boolean toSuiteStack, Boolean fromSuiteStack){
        String moveChecking;
        String moveCheckingReverse;
        Boolean fromStackToStack = false;

        if(fromSuiteStack){
            //FROM A SUITE TO A FIELD STACK
            moveChecking = cardPlacing+">suited";
            moveCheckingReverse = "suited>"+cardPlacing;
        }else if(toSuiteStack){
            //FROM A FIELD STACK TO A SUITE
            moveChecking = cardPlacing+">suited";
            moveCheckingReverse = "suited>"+cardPlacing;
        }else{
            //FROM STACK TO STACK
            fromStackToStack = true;
            moveChecking = cardPlacing+">"+cardTo;
            moveCheckingReverse = cardTo+">"+cardPlacing;
        }

        if(!movesList.contains(moveChecking) && !movesList.contains(moveCheckingReverse)){
            givePoints(toSuiteStack, fromStackToStack);
        }
        movesList.add(moveChecking);
    }

    private void givePoints(Boolean toSuiteStack, Boolean fromStackToStack){

        int bonusPoints = 0;

        if(movesThisTurn >= 2){
            bonusPoints = 15+(10*(movesThisTurn-1));
        }

        Log.d(TAG, "BONUS POINTS: "+bonusPoints);
        Log.d(TAG, "MOVES THIS TURN: "+movesThisTurn);

        if(toSuiteStack){
            //SUITE STACK POINTS
            //ADD BONUS POINTS FOR A leetSwitch
            if(game.logicHelper.lastMove.equals("leetSwitch")){
                bonusPoints += 175;
                Log.d(TAG, "LEETSWITCH BONUS POINTS GIVEN: "+175);
            }
            //
            yourScore += 200+bonusPoints;
            Log.d(TAG, "POINTS GIVEN: "+(200+bonusPoints));
        }else{
            //UNIQUE MOVE POINTS
            yourScore += 50+bonusPoints;
            Log.d(TAG, "POINTS GIVEN: "+(50+bonusPoints));
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
