package com.metagaming.allstarsolitare.gameBoard;

import android.util.Log;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;

class ScoreKeeper {

    private String TAG = "SCORE_KEEPER";

    TextView scoreText;

    //
    UniqueMoves uniqueMoves;
    int uniqueMovesThisTurn;
    int timesThroughDeck;
    int yourScore;

    //SCORING DEFINITIONS
    private final int points_unique_move = 30;
    private final int points_ace_pile = 120;
    private final int points_deck_reset_base = 30;
    private final int points_deck_reset_increment = 100;
    private final int moves_until_combo = 2;
    private final int points_combo_base = 15;
    private final int points_combo_increment = 10;
    private final int points_split_maneuver = 100;

    //
    void init(Game tempGame){
        scoreText = tempGame.mainLayout.findViewById(R.id.game_board_score_text);
        uniqueMoves = new UniqueMoves();
        uniqueMoves.init();
        uniqueMovesThisTurn = 0;
        yourScore = 0;
        timesThroughDeck = 0;
    }

    //
    void checkForUniqueMove(String cardPlacing, String cardTo, Boolean toSuiteStack,
                            Boolean fromSuiteStack, String bonusType, String bonusParams){
        String moveChecking;
        String moveCheckingReverse;

        if(fromSuiteStack){
            //FROM A SUITE TO A FIELD STACK
            moveChecking = "ss>"+cardPlacing;
            moveCheckingReverse = cardPlacing+">ss";
        }else if(toSuiteStack){
            //FROM DECK OR FIELD STACK TO A SUITE
            moveChecking = cardPlacing+">ss";
            moveCheckingReverse = "ss>"+cardPlacing;
        }else{
            //FROM STACK TO STACK
            moveChecking = cardPlacing+">"+cardTo;
            moveCheckingReverse = cardTo+">"+cardPlacing;
        }

        if(!uniqueMoves.containsMove(moveChecking) && !uniqueMoves.containsMove(moveCheckingReverse)){
            //IF MOVE WAS UNIQUE, GIVE POINTS AND ADD MOVE TO uniqueMovesList
            uniqueMoves.addMove(moveChecking, bonusType, bonusParams);
            if(!bonusType.equals("splitManeuver")){
                uniqueMove(toSuiteStack, cardPlacing);
            }
        }
    }

    //
    private void uniqueMove(Boolean toSuiteStack, String cardPlacing){

        int bonusPoints = 0;
        int pointsMade;

        if(uniqueMovesThisTurn >= moves_until_combo){
            bonusPoints = points_combo_base+(points_combo_increment*(uniqueMovesThisTurn -1));
        }

        Log.d(TAG, "BONUS POINTS: "+bonusPoints);
        Log.d(TAG, "MOVES THIS TURN: "+ uniqueMovesThisTurn);

        if(toSuiteStack){
            //SUITE STACK POINTS
            if(uniqueMoves.checkForSplitManeuverSuite(cardPlacing)){
                bonusPoints += points_split_maneuver;
                Log.d(TAG, "SPLIT MANEUVER POINTS: "+points_split_maneuver);
            }
            pointsMade = points_ace_pile+bonusPoints;
        }else{
            //UNIQUE MOVE POINTS
            pointsMade = points_unique_move+bonusPoints;
        }

        Log.d(TAG, "POINTS GIVEN: "+pointsMade);
        yourScore += pointsMade;
        uniqueMoves.uniqueMovesList.get(uniqueMoves.uniqueMovesList.size()-1)[3] = pointsMade;
        scoreText.setText(String.valueOf(yourScore));

        uniqueMovesThisTurn++;
    }

    void drawnFromDeck(){
        uniqueMovesThisTurn = 0;
    }

    void resetDeck(){
        //REMOVE POINTS FOR TURNING DECK OVER
        int pointsToRemove = (points_deck_reset_base+(timesThroughDeck*points_deck_reset_increment));
        if(pointsToRemove > 0){
            yourScore -= pointsToRemove;
            scoreText.setText(String.valueOf(yourScore));
        }
        //
        timesThroughDeck++;
        uniqueMovesThisTurn = 0;
    }
}