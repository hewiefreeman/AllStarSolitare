package com.metagaming.allstarsolitare.gameBoard;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.metagaming.allstarsolitare.R;

class Game {

    //GAME BOARD LAYOUT
    FrameLayout mainLayout;
    FrameLayout cardsHolder;
    private Context gameContext;

    //SCREEN SIZE
    int screenWidth;
    int screenHeight;

    //MARGINS
    int smallMargin = 3;
    int largeMargin = 5;
    int stackTopOffsetSmall;
    int stackTopOffsetRevealed;

    //CARDS
    Deck deck;

    //SET CARD HEIGHT/WIDTH
    final int defaultCardWidth = 126;
    final int defaultCardHeight = 201;

    //CARD KEY LOCATIONS
    Point deckLocation = new Point();
    Point handLocationSingle = new Point();
    Point handLocationThree = new Point();

    Point fieldStackLocation1 = new Point();
    Point fieldStackLocation2 = new Point();
    Point fieldStackLocation3 = new Point();
    Point fieldStackLocation4 = new Point();
    Point fieldStackLocation5 = new Point();
    Point fieldStackLocation6 = new Point();
    Point fieldStackLocation7 = new Point();

    Point clubsPileLocation = new Point();
    Point spadesPileLocation = new Point();
    Point heartsPileLocation = new Point();
    Point diamondsPileLocation = new Point();

    //DECK RESET BTN + ACE PILE BACKGROUND
    CardView deckResetBtn;
    private ImageView deckResetImage;
    private CardView acePileBackground;

    //HELPERS
    CardHelper cardHelper;
    AnimationHelper animationHelper;
    LogicHelper logicHelper;
    GameBoardTimer gameBoardTimer;
    ScoreKeeper scoreKeeper;

    // GAME TYPE
    // SINGLE DRAW = 1
    // THREE DRAW = 2
    int gameType;

    //SETUP VARS
    int setupCardOn;
    int setupStackOn;
    int setupStacksCompleted;
    int setupTopOffset;
    Boolean inSetup;
    CountDownTimer setUpTimer;
    int setupRestores;

    //
    Boolean gameInited = false;

    void initGame(Context context, Boolean gameRestored){
        gameContext = context;
        inSetup = false;
        setupRestores = 0;

        //HELPERS
        cardHelper = new CardHelper();
        cardHelper.init(gameContext, cardsHolder);
        animationHelper = new AnimationHelper();
        logicHelper = new LogicHelper();
        logicHelper.init(this, gameContext);
        gameBoardTimer = new GameBoardTimer();
        scoreKeeper = new ScoreKeeper();
        scoreKeeper.init(this);

        //WHIP OUT AND SHUFFLE THE DECK
        deck = new Deck();
        deck.whipOutTheDeck();
        if(!gameRestored){
            deck.shuffleTheDeck();
        }

        //
        deckResetBtn = mainLayout.findViewById(R.id.game_board_reset_deck);
        deckResetImage = mainLayout.findViewById(R.id.game_board_reset_deck_image);
        acePileBackground = mainLayout.findViewById(R.id.game_board_ace_pile);
    }

    void setUpGame(){
        //INIT VARIABLES
        inSetup = true;
        initVariables();
        setupCardOn = 0;

        //ADD CARDS TO DECK
        for(int i = 0; i < 52; i++){
            deck.setId(cardHelper.createCard(deckLocation.x, deckLocation.y), deck.deckStack.get(i));
            //Log.d("DECK","CARD NAME: "+deck.deckStack.get(i)+"    --    CARD ID: "+deck.getCardId(deck.deckStack.get(i)));
        }

        setupStackOn = 1;
        setupStacksCompleted = 0;
        setupTopOffset = 0;
        setUpTimer = makeSetupTimer(4000).start();
    }

    CountDownTimer makeSetupTimer(int time){
        return new CountDownTimer(time, 100) {

            @Override
            public void onTick(long tick) {
                if(setupCardOn < 28){
                    setUpAnim(setupStackOn, setupTopOffset);

                    //
                    setupCardOn++;
                    if(setupStackOn < 7){
                        setupStackOn++;
                    }else{
                        setupTopOffset = setupTopOffset+stackTopOffsetSmall;
                        setupStacksCompleted++;
                        setupStackOn = 1+setupStacksCompleted;

                    }
                }
            }

            @Override
            public void onFinish() {
                initGame();
            }
        };
    }

    private void setUpAnim(int stackOn, int topOffset){
        int cardNumb = (deck.deckStack.size()-1);
        String cardName = deck.deckStack.get(cardNumb);

        //
        logicHelper.getFieldStackList(stackOn).add(deck.deckStack.get(cardNumb));
        deck.deckStack.remove(cardNumb);

        //Log.d("GAME", "STACK ON: "+stackOn+"  -  CARD ON: "+cardNumb+"  -  CARD NAME: "+cardName);

        //
        CardView tempCard = mainLayout.findViewById(deck.getCardId(cardName));
        tempCard.bringToFront();
        ObjectAnimator animationX = animationHelper.makeAnimator(tempCard, 300, View.X, tempCard.getX(), logicHelper.getFieldStackLocation(stackOn, this).x);
        ObjectAnimator animationY = animationHelper.makeAnimator(tempCard, 300, View.Y, tempCard.getY(), logicHelper.getFieldStackLocation(stackOn, this).y+topOffset);
        animationX.start();
        animationY.start();
    }

    void initGame(){
        //START TIMER
        gameBoardTimer.initTimer(mainLayout);

        //FLIP STARTING CARDS
        for(int i = 0; i < 7; i++){
            String cardName = logicHelper.getFieldStackList(i+1).get(i);
            CardView tempCard = mainLayout.findViewById(deck.getCardId(cardName));
            ImageView tempImage = mainLayout.findViewById(deck.getImageId(cardName));
            animationHelper.flipCard(tempCard, tempImage, deck.getResourceId(cardName), gameContext);
            deck.setCardFacingUp(cardName, true);
            new CardTouchListener().init(tempCard, this, gameContext);
        }

        //INIT DECK
        CardView topDeckCard = mainLayout.findViewById(deck.getCardId(deck.deckStack.get(deck.deckStack.size()-1)));
        new CardTouchListener().init(topDeckCard, this, gameContext);

        //DRAW FIRST CARD(S)
        logicHelper.drawFromDeck();

        //
        gameInited = true;
        inSetup = false;
    }

    void initVariables(){
        float cardRatio = (float) defaultCardHeight/defaultCardWidth;
        Log.d("GAME", "RATIO IS: "+cardRatio);
        final float cardShrinkAmount = defaultCardWidth-((screenWidth-((smallMargin*5)+(largeMargin*2)))/7);
        cardHelper.cardWidth = Math.round(defaultCardWidth-cardShrinkAmount);
        cardHelper.cardHeight = Math.round(cardHelper.cardWidth*(cardRatio))/*+Math.round(cardShrinkAmount*(cardRatio))*/;

        //
        stackTopOffsetSmall = cardHelper.cardHeight/10;
        stackTopOffsetRevealed = cardHelper.cardHeight/3;

        //SET CAMERA DISTANCE
        final float scale = gameContext.getResources().getDisplayMetrics().density;
        cardHelper.cameraDistance = 4000*scale;

        //SET CARD STACK LOCATIONS
        deckLocation.set((screenWidth-cardHelper.cardWidth)-largeMargin, (int)(largeMargin*1.5));
        fieldStackLocation1.set(largeMargin, deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation2.set(largeMargin+cardHelper.cardWidth+smallMargin, deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation3.set(largeMargin+(cardHelper.cardWidth*2)+(smallMargin*2), deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation4.set(largeMargin+(cardHelper.cardWidth*3)+(smallMargin*3), deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation5.set(largeMargin+(cardHelper.cardWidth*4)+(smallMargin*4), deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation6.set(largeMargin+(cardHelper.cardWidth*5)+(smallMargin*5), deckLocation.y+cardHelper.cardHeight+largeMargin);
        fieldStackLocation7.set(largeMargin+(cardHelper.cardWidth*6)+(smallMargin*6), deckLocation.y+cardHelper.cardHeight+largeMargin);
        spadesPileLocation.set(largeMargin, deckLocation.y);
        clubsPileLocation.set(largeMargin+cardHelper.cardWidth+smallMargin, deckLocation.y);
        heartsPileLocation.set(largeMargin+(cardHelper.cardWidth*2)+(smallMargin*2), deckLocation.y);
        diamondsPileLocation.set(largeMargin+(cardHelper.cardWidth*3)+(smallMargin*3), deckLocation.y);
        deckLocation.set(fieldStackLocation7.x, (int)(largeMargin*1.5));
        handLocationSingle.set((int)(deckLocation.x-(cardHelper.cardWidth*1.2)), deckLocation.y);
        handLocationThree.set((deckLocation.x-(cardHelper.cardWidth*2))-smallMargin, deckLocation.y);

        //SET CARD RESET BUTTON
        deckResetBtn.setX(deckLocation.x);
        deckResetBtn.setY(deckLocation.y);
        CardView.LayoutParams deckResetParams = new CardView.LayoutParams(cardHelper.cardWidth, cardHelper.cardHeight);
        deckResetBtn.setLayoutParams(deckResetParams);
        deckResetImage.setLayoutParams(deckResetParams);

        //SET ACE PILE BACKGROUND
        acePileBackground.setX(spadesPileLocation.x-smallMargin);
        acePileBackground.setY(spadesPileLocation.y-smallMargin);
        CardView.LayoutParams acePileBGParams = new CardView.LayoutParams((cardHelper.cardWidth*4)+(smallMargin*5), cardHelper.cardHeight+(smallMargin*2));
        acePileBackground.setLayoutParams(acePileBGParams);
    }
}