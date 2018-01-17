package com.metagaming.allstarsolitare.gameBoard;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.metagaming.allstarsolitare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class LogicHelper {

    private Game game;
    private Context context;
    private final String TAG = "LOGIC_HELP";

    //
    int deckCardOn;
    int cardsInHandUsed;

    //
    int draggingCardId;

    void init(Game tempGame, Context tempContext){
        game = tempGame;
        context = tempContext;

        //
        deckCardOn = 0;
        draggingCardId = -1;
        cardsInHandUsed = 0;
    }

    void checkDrag(int x, int y, String cardName){
        if(y >= game.fieldStackLocation1.y){
            //DRAGGED ONTO FIELD STACK...
            if((x > game.fieldStackLocation1.x) && (x < game.fieldStackLocation1.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 1);

            }else if((x > game.fieldStackLocation2.x) && (x < game.fieldStackLocation2.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 2);

            }else if((x > game.fieldStackLocation3.x) && (x < game.fieldStackLocation3.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 3);

            }else if((x > game.fieldStackLocation4.x) && (x < game.fieldStackLocation4.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 4);

            }else if((x > game.fieldStackLocation5.x) && (x < game.fieldStackLocation5.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 5);

            }else if((x > game.fieldStackLocation6.x) && (x < game.fieldStackLocation6.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 6);

            }else if((x > game.fieldStackLocation7.x) && (x < game.fieldStackLocation7.x+game.viewInflateHelper.cardWidth)){
                droppedCardOverCardStack(cardName, 7);

            }else{
                invalidMatch(cardName);
            }
        }if(y < game.fieldStackLocation1.y){
            if(x >= game.spadesPileLocation.x
                    && x <= game.diamondsPileLocation.x+game.viewInflateHelper.cardWidth){
                //DRAGGED INTO SUITE AREA
                droppedCardOverAceStack(cardName);
            }else{
                invalidMatch(cardName);
            }
        }else{
            invalidMatch(cardName);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //   VALID MATCH CHECKERS   //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void droppedCardOverAceStack(String cardName){
        String fromStackName = game.deck.getCardStackLocation(cardName);

        if(!fromStackName.equals("deck")
                && !fromStackName.equals("club")
                && !fromStackName.equals("spade")
                && !fromStackName.equals("heart")
                && !fromStackName.equals("diamond")){

            List<String> fromStack = getFieldStackList(fromStackName);

            //CANT MOVE INNER STACK CARDS TO ACE PILES, CHEATER.
            if(!fromStack.get(fromStack.size() - 1).equals(cardName)){
                //INVALID. NEXT!
                invalidMatch(cardName);
                return;
            }
        }else if(fromStackName.equals("club")
                || fromStackName.equals("spade")
                || fromStackName.equals("heart")
                || fromStackName.equals("diamond")){

            //CANT MOVE FROM ACE PILE TO ACE PILE
            invalidMatch(cardName);
            return;
        }

        String[] nameSplit = cardName.split("_");
        //noinspection IfCanBeSwitch
        if(nameSplit[1].equals("s")){
            //TRY SPADES STACK
            if(game.deck.spadeStack.size() == 0 && nameSplit[0].equals("a")){
                //VALID ENTRY
                validSuiteStackMatch(cardName, game.deck.spadeStack, game.spadesPileLocation);
            }else if(game.deck.spadeStack.size() > 0){
                if(checkOrder(cardName, game.deck.spadeStack.get(game.deck.spadeStack.size()-1)) == 1){
                    //VALID ENTRY
                    validSuiteStackMatch(cardName, game.deck.spadeStack, game.spadesPileLocation);
                }else{
                    invalidMatch(cardName);
                }
            }else{
                invalidMatch(cardName);
            }

        }else if(nameSplit[1].equals("c")){
            //TRY CLUBS STACK
            if(game.deck.clubStack.size() == 0 && nameSplit[0].equals("a")){
                //VALID ENTRY
                validSuiteStackMatch(cardName, game.deck.clubStack, game.clubsPileLocation);
            }else if(game.deck.clubStack.size() > 0){
                if(checkOrder(cardName, game.deck.clubStack.get(game.deck.clubStack.size()-1)) == 1){
                    //VALID ENTRY
                    validSuiteStackMatch(cardName, game.deck.clubStack, game.clubsPileLocation);
                }else{
                    invalidMatch(cardName);
                }
            }else{
                invalidMatch(cardName);
            }

        }else if(nameSplit[1].equals("h")){
            //TRY HEARTS STACK
            if(game.deck.heartStack.size() == 0 && nameSplit[0].equals("a")){
                //VALID ENTRY
                validSuiteStackMatch(cardName, game.deck.heartStack, game.heartsPileLocation);
            }else if(game.deck.heartStack.size() > 0){
                if(checkOrder(cardName, game.deck.heartStack.get(game.deck.heartStack.size()-1)) == 1){
                    //VALID ENTRY
                    validSuiteStackMatch(cardName, game.deck.heartStack, game.heartsPileLocation);
                }else{
                    invalidMatch(cardName);
                }
            }else{
                invalidMatch(cardName);
            }

        }else if(nameSplit[1].equals("d")){
            //TRY DIAMONDS STACK
            if(game.deck.diamondStack.size() == 0 && nameSplit[0].equals("a")){
                //VALID ENTRY
                validSuiteStackMatch(cardName, game.deck.diamondStack, game.diamondsPileLocation);
            }else if(game.deck.diamondStack.size() > 0){
                if(checkOrder(cardName, game.deck.diamondStack.get(game.deck.diamondStack.size()-1)) == 1){
                    //VALID ENTRY
                    validSuiteStackMatch(cardName, game.deck.diamondStack, game.diamondsPileLocation);
                }else{
                    invalidMatch(cardName);
                }
            }else{
                invalidMatch(cardName);
            }
        }
    }

    private void droppedCardOverCardStack(String cardName, int intoStack){
        List<String> stackList = getFieldStackList(intoStack);

        //CANT DROP BACK INTO SAME STACK LIST
        if((intoStack+"").equals(game.deck.getCardStackLocation(cardName))){
            invalidMatch(cardName);
        }

        if(cardName.split("_")[0].equals("a")){
            invalidMatch(cardName);
        }else if(stackList.size() == 0){
            //MOVING TO EMPTY STACK
            if(cardName.split("_")[0].equals("k")){
                //VALID MATCH
                validCardStackMatch(cardName, intoStack);
            }else{
                invalidMatch(cardName);
            }
        }else if(!game.deck.getIsFacingUp(stackList.get(stackList.size()-1))){
            //MOVING TO STACK WITH A FACE-DOWN CARD ON TOP
            if(cardName.split("_")[0].equals("k")){
                //VALID MATCH
                validCardStackMatch(cardName, intoStack);
            }else{
                invalidMatch(cardName);
            }
        }else if(game.deck.getIsFacingUp(stackList.get(stackList.size()-1))){
            //MOVING TO STACK WITH A FACE-UP CARD ON TOP
            if(!game.deck.getCardColor(cardName).equals(game.deck.getCardColor(stackList.get(stackList.size()-1)))
                    && checkOrder(cardName, stackList.get(stackList.size()-1)) == -1
                    && !stackList.get(stackList.size()-1).split("_")[0].equals("a")){
                //VALID MATCH
                validCardStackMatch(cardName, intoStack);
            }else{
                invalidMatch(cardName);
            }
        }else{
            invalidMatch(cardName);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //   VALID MATCH HANDLERS   //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("ClickableViewAccessibility")
    private void validSuiteStackMatch(String cardName, List<String> toSuit, Point location){
        String stackFrom = game.deck.getCardStackLocation(cardName);
        List<String> cardStackFrom = getFieldStackList(stackFrom);
        CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardName));
        int cardIndex = game.deck.getCardStackIndex(cardName, cardStackFrom);

        //MAKE ANIMATION
        ObjectAnimator animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), location.x);
        ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), location.y);
        animX.start();
        animY.start();

        //GIVE SUITE STACK THE CARD
        toSuit.add(cardStackFrom.get(cardIndex));
        cardStackFrom.remove(cardIndex);

        //MAKE PREV TOP CARD UNCLICKABLE
        if(toSuit.size() - 2 >= 0){
            String prevCardName = toSuit.get(toSuit.size() - 2);
            CardView prevCard = game.mainLayout.findViewById(game.deck.getCardId(prevCardName));
            prevCard.setOnTouchListener(null);
        }

        //
        if(stackFrom.equals("1")
                || stackFrom.equals("2")
                || stackFrom.equals("3")
                || stackFrom.equals("4")
                || stackFrom.equals("5")
                || stackFrom.equals("6")
                || stackFrom.equals("7")){
            //MOVING FROM FIELD STACK
            checkForAvailableCardInFieldStack(Integer.parseInt(stackFrom));
        }else{
            //MOVING FROM DECK
            deckCardOn--;
            makePrevDeckCardClickable();
        }

        //SCORING
        if(toSuit.size()-2 >= 0){
            game.scoreKeeper.checkForUniqueMove(cardName, toSuit.get(toSuit.size() - 2), true, 0, false, "", "");
        }else{
            game.scoreKeeper.checkForUniqueMove(cardName, "empty", true, 0, false, "", "");
        }
    }

    private void validCardStackMatch(String cardName, int stackNumbTo){
        String stackFrom = game.deck.getCardStackLocation(cardName);
        List<String> cardStackFrom = getFieldStackList(stackFrom);
        List<String> cardStackTo = getFieldStackList(stackNumbTo);
        List<Integer> numberOfFacingUpAndDownCardsInFieldStack = game.deck.getNumberOfFacingUpAndDownCardsInFieldStack(cardStackTo);
        int facingUp = numberOfFacingUpAndDownCardsInFieldStack.get(0);
        int facingDown = numberOfFacingUpAndDownCardsInFieldStack.get(1);

        int cardIndex = game.deck.getCardStackIndex(cardName, getFieldStackList(stackFrom));
        Boolean cameFromFieldStack = false;
        Boolean cameFromSuiteStack = false;

        String bonusType = "";
        String bonusParams = "";

        //
        if(stackFrom.equals("deck") || stackFrom.equals("club") || stackFrom.equals("spade") || stackFrom.equals("heart") || stackFrom.equals("diamond")){
            //CAME FROM DECK OR SUITE STACK
            //ANIMATE CARD TO POSITION
            CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardStackFrom.get(cardIndex)));

            ObjectAnimator animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), getFieldStackLocation(stackNumbTo, game).x);
            ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), getFieldStackLocation(stackNumbTo, game).y
                    +(facingUp*game.stackTopOffsetRevealed)
                    +(facingDown*game.stackTopOffsetSmall));

            animX.start();
            animY.start();

            //GIVE CARD TO FIELD STACK
            cardStackTo.add(cardStackFrom.get(cardIndex));
            cardStackFrom.remove(cardIndex);

            if(stackFrom.equals("deck")){
                //
                deckCardOn--;
                makePrevDeckCardClickable();
            }else{
                //CAME FROM SUITE STACK, SO MAKE TOP CARD CLICKABLE
                String topSuiteCardName = cardStackFrom.get(cardStackFrom.size()-1);
                CardView topSuiteCard = game.mainLayout.findViewById(game.deck.getCardId(topSuiteCardName));
                new CardTouchListener().init(topSuiteCard, game, context);
                cameFromSuiteStack = true;
            }

        }else{
            //CAME FROM FIELD STACK
            cameFromFieldStack = true;
            //ANIMATE CARD(S) TO POSITION(S)
            for(int i = cardIndex; i < cardStackFrom.size(); i++){
                CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardStackFrom.get(i)));

                ObjectAnimator animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), getFieldStackLocation(stackNumbTo, game).x);
                ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), getFieldStackLocation(stackNumbTo, game).y
                        +((i - cardIndex) * game.stackTopOffsetRevealed)
                        +(facingUp*game.stackTopOffsetRevealed)
                        +(facingDown*game.stackTopOffsetSmall));

                animX.start();
                animY.start();
            }

            //GIVE CARD(S) TO FIELD STACK
            int stackSize = cardStackFrom.size();
            for(int i = cardIndex; i < stackSize; i++){
                Log.d(TAG, "ADDING '"+cardStackFrom.get(i)+"' TO STACK "+stackNumbTo);
                cardStackTo.add(cardStackFrom.get(i));
            }
            for(int i = cardIndex; i < stackSize; i++){
                Log.d(TAG, "REMOVING '"+cardStackFrom.get(cardStackFrom.size()-1)+"' FROM STACK "+stackFrom);
                cardStackFrom.remove(cardStackFrom.size()-1);
            }

            checkForAvailableCardInFieldStack(Integer.parseInt(stackFrom));
        }
        //SCORE JUDGEMENT
        if(cameFromFieldStack){
            //FIRST CHECK IF THE STACK FROM IS EMPTY NOW, WHICH MEANS bonusType IS "kingSwap"
            if(cardStackFrom.size() == 0){
                //STACK FROM IS EMPTY
                bonusType = "kingSwap";
                bonusParams = stackFrom;

            }else{
                //STACK FROM IS NOT EMPTY
                //CHECK IF TOP CARD OF STACK FROM IS FACE UP
                Boolean card1Up = false;
                if(cardStackFrom.size() - 1 >= 0){
                    if(game.deck.getIsFacingUp(cardStackFrom.get(cardStackFrom.size() - 1))){
                        card1Up = true;
                    }
                }
                //CHECK IF THERE WERE FACING UP CARDS IN THE STACK TO BEFORE MOVE
                Boolean card2Up = false;
                if(facingUp >= 1){
                    card2Up = true;
                }
                //IF BOTH CARDS ARE UP, MOVE WAS A SPLIT MANEUVER
                if(card1Up && card2Up){
                    bonusType = "splitManeuver";
                    bonusParams = cardStackFrom.get(cardStackFrom.size() - 1);
                    Log.d(TAG, "SPLIT MANEUVER: " + bonusParams);
                }
            }
        }

        //
        if(cardStackTo.size()-2 >= 0){
            if(game.deck.getIsFacingUp(cardStackTo.get(cardStackTo.size() - 2))){
                game.scoreKeeper.checkForUniqueMove(cardName, cardStackTo.get(cardStackTo.size() - 2), false, stackNumbTo, cameFromSuiteStack, bonusType, bonusParams);
            }else{
                game.scoreKeeper.checkForUniqueMove(cardName, "empty", false, stackNumbTo, cameFromSuiteStack, bonusType, bonusParams);
            }
        }else{
            game.scoreKeeper.checkForUniqueMove(cardName, "empty", false, stackNumbTo, cameFromSuiteStack, bonusType, bonusParams);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //   INVALID MATCH HANDLER   /////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void invalidMatch(String cardName) {
        String stackFrom = game.deck.getCardStackLocation(cardName);
        String cardStackLocation = game.deck.getCardStackLocation(cardName);
        List<String> cardStack = getFieldStackList(cardStackLocation);

        //noinspection IfCanBeSwitch
        if(stackFrom.equals("deck")){
            //CAME FROM DECK
            CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardName));

            ObjectAnimator animX;

            //CHANGE X ACCORDING TO HAND FOR THREE DRAW
            if(game.gameType == 1){
                //SINGLE DRAW
                animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), getFieldStackLocation(cardStackLocation, game).x);
            }else{
                //THREE DRAW
                int tempX = game.handLocationThree.x+(game.stackTopOffsetRevealed*cardsInHandUsed);
                animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), tempX);
            }

            ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), getFieldStackLocation(cardStackLocation, game).y);

            animX.start();
            animY.start();

        }else if(stackFrom.equals("club") || stackFrom.equals("spade") || stackFrom.equals("heart") || stackFrom.equals("diamond")){
            //CAME FROM SUITE STACK
            CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardName));

            ObjectAnimator animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), getFieldStackLocation(cardStackLocation, game).x);
            ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), getFieldStackLocation(cardStackLocation, game).y);

            animX.start();
            animY.start();

        }else{
            //CAME FROM A FIELD STACK
            int cardIndex = game.deck.getCardStackIndex(cardName, cardStack);
            List<Integer> numberOfFacingUpAndDownCardsInFieldStack = game.deck.getNumberOfFacingUpAndDownCardsInFieldStack(cardStack);
            int facingUp = numberOfFacingUpAndDownCardsInFieldStack.get(0)-(cardStack.size()-cardIndex);
            int facingDown = numberOfFacingUpAndDownCardsInFieldStack.get(1);
            for(int i = cardIndex; i < cardStack.size(); i++){
                CardView cardView = game.mainLayout.findViewById(game.deck.getCardId(cardStack.get(i)));

                ObjectAnimator animX = game.animationHelper.makeAnimator(cardView, 200, View.X, cardView.getX(), getFieldStackLocation(cardStackLocation, game).x);
                ObjectAnimator animY = game.animationHelper.makeAnimator(cardView, 200, View.Y, cardView.getY(), getFieldStackLocation(cardStackLocation, game).y
                        +((i - cardIndex) * game.stackTopOffsetRevealed)
                        +(facingUp*game.stackTopOffsetRevealed)
                        +(facingDown*game.stackTopOffsetSmall));

                animX.start();
                animY.start();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //   DECK LOGIC   ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    void drawFromDeck(){
        if(game.gameType == 1){
            // SINGLE DRAW
            String topDeckCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn+1));
            CardView topDeckCard = game.mainLayout.findViewById(game.deck.getCardId(topDeckCardName));
            ImageView topDeckCardImage = game.mainLayout.findViewById(game.deck.getImageId(topDeckCardName));
            ObjectAnimator animX = game.animationHelper.makeAnimator(topDeckCard, 200, View.X, topDeckCard.getX(), game.handLocationSingle.x);
            ObjectAnimator animY = game.animationHelper.makeAnimator(topDeckCard, 200, View.Y, topDeckCard.getY(), game.handLocationSingle.y);
            game.animationHelper.flipCard(topDeckCard, topDeckCardImage, game.deck.getResourceId(topDeckCardName), context);
            game.deck.setCardFacingUp(topDeckCardName, true);

            topDeckCard.bringToFront();
            animX.start();
            animY.start();

            //MAKE NEXT CARD CLICKABLE
            if(game.deck.deckStack.size() - (deckCardOn+2) < 0){
                //ENABLE DECK RESET BUTTON
                new CardTouchListener().init(game.deckResetBtn, game, context);

            }else{
                String nextDeckCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn + 2));
                CardView nextDeckCard = game.mainLayout.findViewById(game.deck.getCardId(nextDeckCardName));
                new CardTouchListener().init(nextDeckCard, game, context);
            }

            //
            if(deckCardOn != 0){
                String prevDeckCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn));
                CardView prevDeckCard = game.mainLayout.findViewById(game.deck.getCardId(prevDeckCardName));
                prevDeckCard.setOnTouchListener(null);
            }

            //
            deckCardOn++;

        }else{
            //THREE DRAW
            //MAKE PREV HAND CARD UNCLICLABLE
            int prevHandCardIndex = game.deck.deckStack.size() - (deckCardOn);
            if(prevHandCardIndex <= game.deck.deckStack.size()-1 && prevHandCardIndex >= 0){
                String topCardName = game.deck.deckStack.get(prevHandCardIndex);
                Log.d(TAG, "PREVIOUS TOP CARD OF HAND: " + topCardName);
                CardView topCard = game.mainLayout.findViewById(game.deck.getCardId(topCardName));
                topCard.setOnTouchListener(null);
            }

            //MAKE PREV TOP DECK CARD UNCLICLABLE
            int prevTopDeckCardIndex = game.deck.deckStack.size() - (deckCardOn+1);
            if(prevTopDeckCardIndex <= game.deck.deckStack.size()-1 && prevTopDeckCardIndex >= 0){
                String prevTopDeckCardName = game.deck.deckStack.get(prevTopDeckCardIndex);
                Log.d(TAG, "PREVIOUS TOP CARD OF DECK: " + prevTopDeckCardName);
                CardView prevTopDeckCard = game.mainLayout.findViewById(game.deck.getCardId(prevTopDeckCardName));
                prevTopDeckCard.setOnTouchListener(null);
            }

            //
            cardsInHandUsed = 0;

            //GET CARDS AVAILABLE IN DECK
            final int cardsAvailable;
            if(game.deck.deckStack.size()-(deckCardOn+3) < 0){
                //
                cardsAvailable = game.deck.deckStack.size()-(deckCardOn+3)+3;

            }else if(game.deck.deckStack.size()-(deckCardOn+3) == 0){
                //
                cardsAvailable = 3;

                //ENABLE DECK RESET BUTTON
                new CardTouchListener().init(game.deckResetBtn, game, context);
            }else{
                //
                cardsAvailable = 3;
            }

            //MAKE LAST OF HAND CLICKABLE
            int handCardIndex = game.deck.deckStack.size()-(deckCardOn+cardsAvailable);
            if(handCardIndex <= game.deck.deckStack.size()-1 && handCardIndex >= 0){
                String thirdCardName = game.deck.deckStack.get(handCardIndex);
                Log.d(TAG, "NEW HAND TOP CARD SHOULD BE: " + thirdCardName);
                CardView thirdCard = game.mainLayout.findViewById(game.deck.getCardId(thirdCardName));
                new CardTouchListener().init(thirdCard, game, context);
            }


            //MAKE NEW TOP CARD CLICKABLE
            int newTopCardIndex = game.deck.deckStack.size()-(deckCardOn+4);
            if(newTopCardIndex <= game.deck.deckStack.size()-1 && newTopCardIndex >= 0){
                String newTopCardName = game.deck.deckStack.get(newTopCardIndex);
                Log.d(TAG, "NEW TOP DECK CARD SHOULD BE: " + newTopCardName);
                CardView newTopCard = game.mainLayout.findViewById(game.deck.getCardId(newTopCardName));
                new CardTouchListener().init(newTopCard, game, context);
            }else{
                //ENABLE DECK RESET BUTTON
                new CardTouchListener().init(game.deckResetBtn, game, context);
            }

            //SQUISH LAST HAND
            for(int i = game.deck.deckStack.size()-(deckCardOn+1); i < game.deck.deckStack.size()-1; i++){
                String tempCardName = game.deck.deckStack.get(i);
                CardView tempCard = game.mainLayout.findViewById(game.deck.getCardId(tempCardName));
                ObjectAnimator animX = game.animationHelper.makeAnimator(tempCard, 200, View.X, tempCard.getX(), game.handLocationThree.x+(game.stackTopOffsetRevealed*2));
                ObjectAnimator animY = game.animationHelper.makeAnimator(tempCard, 200, View.Y, tempCard.getY(), game.handLocationThree.y);

                animX.start();
                animY.start();
            }

            //
            new CountDownTimer(1200, 300) {

                int cardOnIndex = game.deck.deckStack.size()-(deckCardOn+1);
                int cardsDrawn = 0;
                int cardOnPosition = cardsAvailable-1;

                @Override
                public void onTick(long mills) {
                    if(cardsDrawn < cardsAvailable){
                        //ANIMATE THE CARDS
                        String cardOnName = game.deck.deckStack.get(cardOnIndex);
                        CardView cardOn = game.mainLayout.findViewById(game.deck.getCardId(cardOnName));
                        ImageView cardOnImage = game.mainLayout.findViewById(game.deck.getImageId(cardOnName));
                        ObjectAnimator animX = game.animationHelper.makeAnimator(cardOn, 200, View.X, cardOn.getX(), game.handLocationThree.x+(game.stackTopOffsetRevealed*cardOnPosition)+(game.stackTopOffsetRevealed*(3-cardsAvailable)));
                        ObjectAnimator animY = game.animationHelper.makeAnimator(cardOn, 200, View.Y, cardOn.getY(), game.handLocationThree.y);
                        game.animationHelper.flipCard(cardOn, cardOnImage, game.deck.getResourceId(cardOnName), context);
                        game.deck.setCardFacingUp(cardOnName, true);

                        animX.start();
                        animY.start();

                        cardOn.bringToFront();

                        //
                        cardOnIndex--;
                        cardsDrawn++;
                        cardOnPosition--;
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();

            //
            deckCardOn = deckCardOn+cardsAvailable;
        }

        game.scoreKeeper.drawnFromDeck();
    }

    private void makePrevDeckCardClickable(){
        if(deckCardOn != 0){
            String prevDeckCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn));
            CardView prevDeckCard = game.mainLayout.findViewById(game.deck.getCardId(prevDeckCardName));
            new CardTouchListener().init(prevDeckCard, game, context);

            if(game.gameType == 2 && cardsInHandUsed < 2){
                cardsInHandUsed++;
            }
        }
    }

    void resetDeck(){
        //
        game.deckResetBtn.setOnTouchListener(null);

        if(game.deck.deckStack.size() > 0){
            String handCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn));
            Log.d(TAG, "RESORTING. TOP CARD WAS: "+handCardName);
            CardView handDeckCard = game.mainLayout.findViewById(game.deck.getCardId(handCardName));
            handDeckCard.setOnTouchListener(null);
        }else{
            return;
        }


        //GO THROUGH DECK
        for(int i = 0; i < game.deck.deckStack.size(); i++){
            String cardName = game.deck.deckStack.get(i);
            CardView deckCard = game.mainLayout.findViewById(game.deck.getCardId(cardName));
            ImageView deckCardImage = game.mainLayout.findViewById(game.deck.getImageId(cardName));

            //TURN CARD AROUND
            game.deck.setCardFacingUp(cardName, false);
            Picasso.with(context).load(R.drawable.back).noFade().into(deckCardImage);

            //PLACE BACK INTO DECK
            deckCard.bringToFront();
            deckCard.setX(game.deckLocation.x);
            deckCard.setY(game.deckLocation.y);
        }

        //
        deckCardOn = 0;

        //MAKE TOP CARD CLICKABLE
        String nextDeckCardName = game.deck.deckStack.get(game.deck.deckStack.size() - (deckCardOn + 1));
        CardView nextDeckCard = game.mainLayout.findViewById(game.deck.getCardId(nextDeckCardName));
        new CardTouchListener().init(nextDeckCard, game, context);

        //
        game.scoreKeeper.resetDeck();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //   COMMON CHECKERS   ///////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkForAvailableCardInFieldStack(int stackNumber){
        if(getFieldStackList(stackNumber).size() > 0){
            String topCard = getFieldStackList(stackNumber).get(getFieldStackList(stackNumber).size() - 1);
            if(!game.deck.getIsFacingUp(topCard)){
                CardView tempCard = game.mainLayout.findViewById(game.deck.getCardId(topCard));
                new CardTouchListener().init(tempCard, game, context);
            }
        }
    }

    Point getFieldStackLocation(int numb, Game gameObj){
        if(numb == 1){
            return gameObj.fieldStackLocation1;
        }else if(numb == 2){
            return gameObj.fieldStackLocation2;
        }else if(numb == 3){
            return gameObj.fieldStackLocation3;
        }else if(numb == 4){
            return gameObj.fieldStackLocation4;
        }else if(numb == 5){
            return gameObj.fieldStackLocation5;
        }else if(numb == 6){
            return gameObj.fieldStackLocation6;
        }else if(numb == 7){
            return gameObj.fieldStackLocation7;
        }

        //
        return new Point();
    }

    private Point getFieldStackLocation(String numb, Game gameObj){
        //noinspection IfCanBeSwitch
        if(numb.equals("1")){
            return gameObj.fieldStackLocation1;
        }else if(numb.equals("2")){
            return gameObj.fieldStackLocation2;
        }else if(numb.equals("3")){
            return gameObj.fieldStackLocation3;
        }else if(numb.equals("4")){
            return gameObj.fieldStackLocation4;
        }else if(numb.equals("5")){
            return gameObj.fieldStackLocation5;
        }else if(numb.equals("6")){
            return gameObj.fieldStackLocation6;
        }else if(numb.equals("7")){
            return gameObj.fieldStackLocation7;
        }else if(numb.equals("deck")){
            if(game.gameType == 1){
                return gameObj.handLocationSingle;
            }else{
                return gameObj.handLocationThree;
            }
        }else if(numb.equals("club")){
            return gameObj.clubsPileLocation;
        }else if(numb.equals("spade")){
            return gameObj.spadesPileLocation;
        }else if(numb.equals("heart")){
            return gameObj.heartsPileLocation;
        }else if(numb.equals("diamond")){
            return gameObj.diamondsPileLocation;
        }

        //
        return new Point();
    }

    List<String> getFieldStackList(int numb){
        if(numb == 1){
            return game.deck.fieldStack1;
        }else if(numb == 2){
            return game.deck.fieldStack2;
        }else if(numb == 3){
            return game.deck.fieldStack3;
        }else if(numb == 4){
            return game.deck.fieldStack4;
        }else if(numb == 5){
            return game.deck.fieldStack5;
        }else if(numb == 6){
            return game.deck.fieldStack6;
        }else if(numb == 7){
            return game.deck.fieldStack7;
        }

        //
        return new ArrayList<>();
    }

    List<String> getFieldStackList(String numb){
        //noinspection IfCanBeSwitch
        if(numb.equals("1")){
            return game.deck.fieldStack1;
        }else if(numb.equals("2")){
            return game.deck.fieldStack2;
        }else if(numb.equals("3")){
            return game.deck.fieldStack3;
        }else if(numb.equals("4")){
            return game.deck.fieldStack4;
        }else if(numb.equals("5")){
            return game.deck.fieldStack5;
        }else if(numb.equals("6")){
            return game.deck.fieldStack6;
        }else if(numb.equals("7")){
            return game.deck.fieldStack7;
        }else if(numb.equals("deck")){
            return game.deck.deckStack;
        }else if(numb.equals("club")){
            return game.deck.clubStack;
        }else if(numb.equals("spade")){
            return game.deck.spadeStack;
        }else if(numb.equals("heart")){
            return game.deck.heartStack;
        }else if(numb.equals("diamond")){
            return game.deck.diamondStack;
        }

        //
        return new ArrayList<>();
    }

    private int checkOrder(String card1, String card2){
        String cardType1 = card1.split("_")[0];
        String cardType2 = card2.split("_")[0];
        int cardIndex1 = 0;
        int cardIndex2 = 0;
        for(int i = 0; i < game.deck.cardsGameOrder.size(); i++){
            if(game.deck.cardsGameOrder.get(i).equals(cardType1)){
                cardIndex1 = i;
            }
        }
        for(int i = 0; i < game.deck.cardsGameOrder.size(); i++){
            if(game.deck.cardsGameOrder.get(i).equals(cardType2)){
                cardIndex2 = i;
            }
        }

        return cardIndex1-cardIndex2;
    }
}
