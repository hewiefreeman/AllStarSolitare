package com.metagaming.allstarsolitare.gameBoard;

import android.util.Log;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;

import java.util.ArrayList;

class ScoreKeeper {

    private String TAG = "SCORE_KEEPER";

    Game game;
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
    private final int points_combo_base = 20;
    private final int points_combo_increment = 15;
    private final int points_split_maneuver = 150;
    private final int points_king_swap = 200;

    //
    void init(Game tempGame){
        game = tempGame;
        scoreText = tempGame.mainLayout.findViewById(R.id.game_board_score_text);
        uniqueMoves = new UniqueMoves();
        uniqueMoves.init();
        uniqueMovesThisTurn = 0;
        yourScore = 0;
        timesThroughDeck = 0;
    }

    //
    void checkForUniqueMove(String cardPlacing, String cardTo, Boolean toSuiteStack, int cardStackTo,
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
                uniqueMove(toSuiteStack, cardStackTo, cardPlacing);
            }
        }
    }

    //
    private void uniqueMove(Boolean toSuiteStack, int cardStackTo, String cardPlacing){

        int bonusPoints = 0;
        int pointsMade;
        ArrayList<Object[]> displayList = new ArrayList<>();
        Object[] comboPoints = null;

        //LIMIT COMBO POINTS TO 10
        if(uniqueMovesThisTurn >= moves_until_combo && uniqueMovesThisTurn < 9+moves_until_combo){
            bonusPoints = points_combo_base+(points_combo_increment*(uniqueMovesThisTurn-(moves_until_combo-1)));

            if(uniqueMovesThisTurn-(moves_until_combo-1) == 1){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_1x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 2){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_2x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 3){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_3x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 4){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_4x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 5){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_5x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 6){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_6x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 7){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_7x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 8){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_8x};
            }else if(uniqueMovesThisTurn-(moves_until_combo-1) == 9){ comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_9x};
            }
        }else if(uniqueMovesThisTurn >= 9+moves_until_combo){
            bonusPoints = points_combo_base+(points_combo_increment*10);
            comboPoints = new Object[]{bonusPoints, R.color.good, R.drawable.combo_10x};
        }

        Log.d(TAG, "BONUS POINTS: "+bonusPoints);
        Log.d(TAG, "MOVES THIS TURN: "+ uniqueMovesThisTurn);

        // displayList Object[] order:
        // 0 (int)  :   points
        // 1 (int)  :   colorID
        // 2 (int)  :   imageID (-1 is no image)

        if(toSuiteStack){
            //SUITE STACK POINTS
            pointsMade = points_ace_pile;
            Object[] pointsSuite = new Object[]{points_ace_pile, R.color.good, -1};
            displayList.add(pointsSuite);
            if(comboPoints != null){
                displayList.add(comboPoints);
            }
            //
            if(uniqueMoves.checkForSplitManeuverSuite(cardPlacing)){
                bonusPoints += points_split_maneuver;
                Object[] splitManeuverSuite = new Object[]{points_split_maneuver, R.color.good, R.drawable.swap_ace};
                displayList.add(splitManeuverSuite);
                Log.d(TAG, "SPLIT MANEUVER POINTS: "+points_split_maneuver);
            }
        }else{
            //UNIQUE MOVE POINTS
            pointsMade = points_unique_move;
            Object[] pointsMove = new Object[]{points_unique_move, R.color.good, -1};
            displayList.add(pointsMove);
            if(comboPoints != null){
                displayList.add(comboPoints);
            }
            //
            if(cardPlacing.split("_")[0].equals("k") && uniqueMoves.checkForKingSwap(cardStackTo)){
                bonusPoints += points_king_swap;
                Object[] kingSwap = new Object[]{points_king_swap, R.color.good, R.drawable.swap_king};
                displayList.add(kingSwap);
                Log.d(TAG, "KING SWAP POINTS: " + points_king_swap);
            }
        }

        Log.d(TAG, "POINTS GIVEN: "+(pointsMade+bonusPoints));
        yourScore += pointsMade+bonusPoints;
        uniqueMoves.uniqueMovesList.get(uniqueMoves.uniqueMovesList.size()-1)[3] = pointsMade;
        scoreText.setText(String.valueOf(yourScore));

        //
        game.viewInflateHelper.createPointsDisplay(displayList);

        //
        uniqueMovesThisTurn++;
    }

    void drawnFromDeck(){
        uniqueMovesThisTurn = 0;
    }

    void resetDeck(){
        //REMOVE POINTS FOR TURNING DECK OVER
        int pointsToRemove = (points_deck_reset_base+(timesThroughDeck*points_deck_reset_increment));
        if(pointsToRemove > 0){
            if(yourScore-pointsToRemove < 0){
                yourScore = 0;
            }else{
                yourScore -= pointsToRemove;
            }
            scoreText.setText(String.valueOf(yourScore));
        }
        //
        timesThroughDeck++;
        uniqueMovesThisTurn = 0;
    }
}