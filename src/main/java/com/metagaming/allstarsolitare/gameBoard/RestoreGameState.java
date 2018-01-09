package com.metagaming.allstarsolitare.gameBoard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;

import com.metagaming.allstarsolitare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


class RestoreGameState {

    void restoreGame(final Game game, final Bundle restoreBundle, Context context){
        final String TAG = "RESTORING";

        //INIT VARIABLES
        game.initVariables();
        Boolean isFromSetup = restoreBundle.getBoolean("fromSetUp");

        if(!isFromSetup){
            //SET cardIsFacingUp FOR ALL THE DECK
            for(int i = 0; i < game.deck.deck.size(); i++){
                //noinspection ConstantConditions
                String[] tempBundleCard = restoreBundle.getString("deckCardIsFacingUp" + i).split(":");

                String cardName = tempBundleCard[0];
                Boolean isFaceUp = false;
                if(tempBundleCard[1].equals("true")){
                    isFaceUp = true;
                }

                game.deck.setCardFacingUp(cardName, isFaceUp);
            }
        }else{
            game.setupRestores = restoreBundle.getInt("setupRestores");
        }

        //PARSE FIELD STACKS
        game.deck.fieldStack1 = restoreBundle.getStringArrayList("fieldStack1");
        game.deck.fieldStack2 = restoreBundle.getStringArrayList("fieldStack2");
        game.deck.fieldStack3 = restoreBundle.getStringArrayList("fieldStack3");
        game.deck.fieldStack4 = restoreBundle.getStringArrayList("fieldStack4");
        game.deck.fieldStack5 = restoreBundle.getStringArrayList("fieldStack5");
        game.deck.fieldStack6 = restoreBundle.getStringArrayList("fieldStack6");
        game.deck.fieldStack7 = restoreBundle.getStringArrayList("fieldStack7");

        if(!isFromSetup){
            //PARSE SUITE STACKS
            game.deck.clubStack = restoreBundle.getStringArrayList("clubStack");
            game.deck.spadeStack = restoreBundle.getStringArrayList("spadeStack");
            game.deck.diamondStack = restoreBundle.getStringArrayList("diamondStack");
            game.deck.heartStack = restoreBundle.getStringArrayList("heartStack");

            //YOUR HAND
            game.logicHelper.deckCardOn = restoreBundle.getInt("deckCardOn");
            game.logicHelper.cardsInHandUsed = restoreBundle.getInt("cardsInHandUsed");
        }else{
            game.deck.clubStack = new ArrayList<>();
            game.deck.spadeStack = new ArrayList<>();
            game.deck.diamondStack = new ArrayList<>();
            game.deck.heartStack = new ArrayList<>();

            //YOUR HAND
            game.logicHelper.deckCardOn = 0;
            game.logicHelper.cardsInHandUsed = 0;
        }

        //PARSE DECK
        game.deck.deckStack = restoreBundle.getStringArrayList("deckStack");

        //FIELD STACK COUNTERS
        int cardTopMargin;

        //POPULATE FIELD STACK 1
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack1.size(); i++){
            String cardName = game.deck.fieldStack1.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack1.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation1.x, game.fieldStackLocation1.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack1.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 2
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack2.size(); i++){
            String cardName = game.deck.fieldStack2.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack2.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation2.x, game.fieldStackLocation2.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack2.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 3
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack3.size(); i++){
            String cardName = game.deck.fieldStack3.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack3.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation3.x, game.fieldStackLocation3.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack3.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 4
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack4.size(); i++){
            String cardName = game.deck.fieldStack4.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack4.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation4.x, game.fieldStackLocation4.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack4.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 5
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack5.size(); i++){
            String cardName = game.deck.fieldStack5.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack5.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation5.x, game.fieldStackLocation5.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack5.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 6
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack6.size(); i++){
            String cardName = game.deck.fieldStack6.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack6.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation6.x, game.fieldStackLocation6.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack6.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE FIELD STACK 7
        cardTopMargin = 0;
        for(int i = 0; i < game.deck.fieldStack7.size(); i++){
            String cardName = game.deck.fieldStack7.get(i);
            if(i-1 >= 0){
                if(game.deck.getIsFacingUp(game.deck.fieldStack7.get(i-1))){
                    cardTopMargin = cardTopMargin + game.stackTopOffsetRevealed;
                }else{
                    cardTopMargin = cardTopMargin + game.stackTopOffsetSmall;
                }
            }
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.fieldStackLocation7.x, game.fieldStackLocation7.y+cardTopMargin);

            game.deck.setId(tempStackCard, cardName);

            if(!isFromSetup){
                if(game.deck.getIsFacingUp(cardName)){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                    cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));
                    new CardTouchListener().init(cardView, game, context);
                }else if(i == game.deck.fieldStack7.size() - 1){
                    CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        if(!isFromSetup){
            //POPULATE SPADE STACK
            for(int i = 0; i < game.deck.spadeStack.size(); i++){
                String cardName = game.deck.spadeStack.get(i);
                List<Integer> tempStackCard =
                        game.cardHelper.createCard(game.spadesPileLocation.x, game.spadesPileLocation.y);

                game.deck.setId(tempStackCard, cardName);

                CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));

                if(i == game.deck.spadeStack.size() - 1){
                    new CardTouchListener().init(cardView, game, context);
                }
            }

            //POPULATE CLUB STACK
            for(int i = 0; i < game.deck.clubStack.size(); i++){
                String cardName = game.deck.clubStack.get(i);
                List<Integer> tempStackCard =
                        game.cardHelper.createCard(game.clubsPileLocation.x, game.clubsPileLocation.y);

                game.deck.setId(tempStackCard, cardName);

                CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));

                if(i == game.deck.clubStack.size() - 1){
                    new CardTouchListener().init(cardView, game, context);
                }
            }

            //POPULATE HEART STACK
            for(int i = 0; i < game.deck.heartStack.size(); i++){
                String cardName = game.deck.heartStack.get(i);
                List<Integer> tempStackCard =
                        game.cardHelper.createCard(game.heartsPileLocation.x, game.heartsPileLocation.y);

                game.deck.setId(tempStackCard, cardName);

                CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));

                if(i == game.deck.heartStack.size() - 1){
                    new CardTouchListener().init(cardView, game, context);
                }
            }

            //POPULATE DIAMOND STACK
            for(int i = 0; i < game.deck.diamondStack.size(); i++){
                String cardName = game.deck.diamondStack.get(i);
                List<Integer> tempStackCard =
                        game.cardHelper.createCard(game.diamondsPileLocation.x, game.diamondsPileLocation.y);

                game.deck.setId(tempStackCard, cardName);

                CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));
                ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));
                cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));

                if(i == game.deck.diamondStack.size() - 1){
                    new CardTouchListener().init(cardView, game, context);
                }
            }
        }

        //POPULATE DECK
        int cardsInHandPlaced = 0;
        for(int i = 0; i < game.deck.deckStack.size(); i++){
            String cardName = game.deck.deckStack.get(i);
            List<Integer> tempStackCard =
                    game.cardHelper.createCard(game.deckLocation.x, game.deckLocation.y);

            game.deck.setId(tempStackCard, cardName);

            CardView cardView = game.mainLayout.findViewById(tempStackCard.get(0));

            if(game.deck.getIsFacingUp(cardName)){

                ImageView cardImage = game.mainLayout.findViewById(tempStackCard.get(1));

                cardImage.setImageDrawable(context.getResources().getDrawable(game.deck.getResourceId(cardName)));

                if(game.gameType == 1){
                    cardView.setX(game.handLocationSingle.x);
                    cardView.setY(game.handLocationSingle.y);
                }else{
                    cardView.setX(game.handLocationThree.x+(game.stackTopOffsetRevealed*2));
                    cardView.setY(game.handLocationThree.y);
                }

                if(game.gameType == 1 && game.deck.deckStack.size()-game.logicHelper.deckCardOn == i){
                    //CARD IN HAND - SINGLE DRAW
                    Log.d(TAG, "CARD IN HAND WAS: "+cardName);
                    new CardTouchListener().init(cardView, game, context);
                }else if(game.gameType == 2){
                    //OFFSET OTHER CARDS IN HAND - THREE DRAW
                    if(i >= game.deck.deckStack.size()-game.logicHelper.deckCardOn
                            && i <= game.deck.deckStack.size()-(game.logicHelper.deckCardOn-(2-game.logicHelper.cardsInHandUsed))){
                        cardView.setX(game.handLocationThree.x+(game.stackTopOffsetRevealed*(cardsInHandPlaced+game.logicHelper.cardsInHandUsed)));

                        Log.d(TAG, "CARD IN HAND("+cardsInHandPlaced+"): "+cardName);

                        cardsInHandPlaced++;
                    }

                    if(i == game.deck.deckStack.size()-game.logicHelper.deckCardOn){
                        //CARD IN HAND AVAILABLE
                        Log.d(TAG, "CARD AVAILABLE IN HAND WAS: "+cardName);
                        new CardTouchListener().init(cardView, game, context);
                    }
                }
            }else if((i == game.deck.deckStack.size()-(game.logicHelper.deckCardOn+1)) && (!isFromSetup)){
                //TOP CARD OF DECK
                Log.d(TAG, "NEXT CARD IN DECK IS: "+game.deck.deckStack.get(game.deck.deckStack.size()-(game.logicHelper.deckCardOn+1)));
                new CardTouchListener().init(cardView, game, context);
            }
        }

        if(!isFromSetup){
            //NEED A DECK? ENABLE DECK RESET BTN
            if(game.deck.deckStack.size() - (game.logicHelper.deckCardOn) == 0){
                new CardTouchListener().init(game.deckResetBtn, game, context);
            }

            //REVERSE Z-ORDER OF CARDS IN HAND
            for(int i = game.deck.deckStack.size() - 1; i > game.deck.deckStack.size() - (game.logicHelper.deckCardOn + 1); i--){
                String cardName = game.deck.deckStack.get(i);
                CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardName));
                cardView.bringToFront();
            }

            //
            game.gameInited = true;

        }else{
            int cardDifference = 28-restoreBundle.getInt("setupCardOn");
            game.inSetup = true;
            game.setupCardOn = restoreBundle.getInt("setupCardOn");
            game.setupStackOn = restoreBundle.getInt("setupStackOn");
            game.setupStacksCompleted = restoreBundle.getInt("setupStacksCompleted");
            if(game.setupRestores == 0){
                game.setupTopOffset = restoreBundle.getInt("setupTopOffset")-game.stackTopOffsetSmall;
            }else{
                game.setupTopOffset = restoreBundle.getInt("setupTopOffset");
            }

            game.setupRestores++;

            if(cardDifference > 0){
                game.setUpTimer = game.makeSetupTimer(cardDifference * 100 + 1000).start();
            }else{
                game.startGame();
            }
        }

        //RESTORE TIME
        game.gameBoardTimer = new GameBoardTimer();
        game.gameBoardTimer.timerText = game.mainLayout.findViewById(R.id.game_board_timer_text);
        game.gameBoardTimer.theTimer = new Timer();
        game.gameBoardTimer.timeElapsed = restoreBundle.getLong("timeElapsed");
        game.gameBoardTimer.startTime = System.currentTimeMillis()-(game.gameBoardTimer.timeElapsed);
        game.gameBoardTimer.startTimer();

        //RESTORE SCORE
        game.scoreKeeper.yourScore = restoreBundle.getInt("yourScore");
        game.scoreKeeper.uniqueMovesThisTurn = restoreBundle.getInt("uniqueMovesThisTurn");
        game.scoreKeeper.timesThroughDeck = restoreBundle.getInt("timesThroughDeck");
        String[] savedMoves = restoreBundle.getStringArray("savedMoves");
        if(savedMoves != null){
            for(String savedMove : savedMoves){
                String[] savedMoveArray = savedMove.split(",");
                game.scoreKeeper.uniqueMoves.addMove(savedMoveArray[0], savedMoveArray[1], savedMoveArray[2]);
                game.scoreKeeper.uniqueMoves.getLastMove()[3] = Integer.parseInt(savedMoveArray[3]);
            }
        }
    }
}
