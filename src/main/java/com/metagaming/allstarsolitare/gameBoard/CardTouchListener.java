package com.metagaming.allstarsolitare.gameBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.util.List;


public class CardTouchListener implements OnTouchListener {

    //private final String TAG = "TOUCH_LSTNR";

    private Game game;
    private Context context;

    private String cardName;
    private int cardDraggingIndex;
    private List<String> draggingStack;
    private Boolean isDraggingStack;
    private float dragStartX;
    private float dragStartY;
    private float cardX;
    private float cardY;

    private Boolean clickedDeckReset;


    void init(CardView cardView, Game tempGame, Context tempContext){
        game = tempGame;
        context = tempContext;
        cardView.setOnTouchListener(this);

        isDraggingStack = false;

        clickedDeckReset = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN
                || motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN){
            if(game.logicHelper.draggingCardId != -1){
                return true;
            }else{
                game.logicHelper.draggingCardId = view.getId();
            }

            if(view.getId() == game.deckResetBtn.getId()){
                clickedDeckReset = true;

                //
                return true;
            }

            if(!game.deck.getIsFacingUp(game.deck.getNameFromId(view.getId()))){
                //VIEW IS FACE DOWN
                cardName = game.deck.getNameFromId(view.getId());

            }else{
                //VIEW IS FACE UP
                cardName = game.deck.getNameFromId(view.getId());
                dragStartX = motionEvent.getRawX();
                dragStartY = motionEvent.getRawY();
                cardX = view.getX();
                cardY = view.getY();
                String cardStackLocation = game.deck.getCardStackLocation(cardName);
                if(cardStackLocation.equals("deck") || cardStackLocation.equals("spade") || cardStackLocation.equals("club") || cardStackLocation.equals("heart") || cardStackLocation.equals("diamond")){
                    //MOVING FROM DECK OR SUITE STACK
                    isDraggingStack = false;
                    cardName = game.deck.getNameFromId(view.getId());
                    int cardId = game.deck.getCardId(cardName);
                    CardView tempCard = game.mainLayout.findViewById(cardId);

                    tempCard.bringToFront();

                }else{
                    //MOVING FROM A FIELD STACK
                    isDraggingStack = true;

                    draggingStack = game.logicHelper.getFieldStackList(cardStackLocation);
                    cardDraggingIndex = game.deck.getCardStackIndex(cardName, game.logicHelper.getFieldStackList(cardStackLocation));

                    for(int i = cardDraggingIndex; i < draggingStack.size(); i++){
                        int cardId = game.deck.getCardId(draggingStack.get(i));
                        CardView tempCard = game.mainLayout.findViewById(cardId);

                        tempCard.bringToFront();
                    }
                }
            }
            //
            return true;

        }else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){

            if(clickedDeckReset){
                return true;
            }else if(view.getId() != game.logicHelper.draggingCardId){
                return true;
            }

            if(game.deck.getIsFacingUp(game.deck.getNameFromId(view.getId()))){
                float x = motionEvent.getRawX() - dragStartX;
                float y = motionEvent.getRawY() - dragStartY;

                //IF THERE ARE MORE THAN ONE CARDS IN STACK, MAKE ALL MOVE
                if(!isDraggingStack && cardName != null && !cardName.isEmpty()){
                    int cardId = game.deck.getCardId(cardName);
                    CardView tempCard = game.mainLayout.findViewById(cardId);

                    tempCard.setX(x + cardX);
                    tempCard.setY(y + cardY);
                }else if(cardName != null && !cardName.isEmpty()){
                    //MOVING FROM A FIELD STACK
                    for(int i = cardDraggingIndex; i < draggingStack.size(); i++){
                        int cardId = game.deck.getCardId(draggingStack.get(i));
                        CardView tempCard = game.mainLayout.findViewById(cardId);

                        tempCard.setX(x + cardX);
                        tempCard.setY(y + cardY + ((i - cardDraggingIndex) * game.stackTopOffsetRevealed));
                    }
                }
            }

            //
            return true;

        }else if(motionEvent.getAction() == MotionEvent.ACTION_UP
                || motionEvent.getAction() == MotionEvent.ACTION_POINTER_UP
                || motionEvent.getAction() == MotionEvent.ACTION_CANCEL){

            if(clickedDeckReset){
                game.logicHelper.resetDeck();

                //
                clickedDeckReset = false;
                game.logicHelper.draggingCardId = -1;

                return true;
            }else if(game.logicHelper.draggingCardId != view.getId()){
                return true;
            }

            if(!game.deck.getIsFacingUp(game.deck.getNameFromId(view.getId()))){
                if(cardName != null && !cardName.isEmpty()){
                    //CLICKED FACE DOWN CARD
                    if(game.deck.getCardStackLocation(cardName).equals("deck")){
                        //DRAW FROM DECK
                        game.logicHelper.drawFromDeck();
                    }else{
                        //FLIP CARD
                        CardView tempCard = game.mainLayout.findViewById(game.deck.getCardId(cardName));
                        ImageView tempImage = game.mainLayout.findViewById(game.deck.getImageId(cardName));
                        game.animationHelper.flipCard(tempCard, tempImage, game.deck.getResourceId(cardName), context);
                        game.deck.setCardFacingUp(cardName, true);
                    }
                }
            }else{
                game.logicHelper.checkDrag((int) view.getX()+(game.cardHelper.cardWidth/2), (int) view.getY()+(game.cardHelper.cardHeight/2), cardName);
            }

            cardName = "";
            cardDraggingIndex = 0;
            dragStartX = 0;
            dragStartY = 0;
            cardX = 0;
            cardY = 0;

            //
            game.logicHelper.draggingCardId = -1;

            //
            return true;

        }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
            if(!game.deck.getIsFacingUp(game.deck.getNameFromId(view.getId()))){
                cardName = "";
            }

            //
            return true;
        }

        //
        return false;
    }
}
