package com.metagaming.allstarsolitare.gameBoard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.metagaming.allstarsolitare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ViewInflateHelper {

    float cameraDistance;
    int cardWidth;
    int cardHeight;
    private Context gameBoardContext;
    private FrameLayout cardsHolder;
    private FrameLayout mainLayout;

    void init(Context tempContext, FrameLayout tempLayout, FrameLayout tempMainLayout){
        gameBoardContext = tempContext;
        mainLayout = tempMainLayout;
        cardsHolder = tempLayout;
    }

    List<Integer> createCard(int x, int y){
        //
        CardView newCard = new CardView(gameBoardContext);
        ImageView imageView = new ImageView(gameBoardContext);

        //SET WIDTH/HEIGHT
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(cardWidth, cardHeight);
        newCard.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(cardWidth, cardHeight);
        imageView.setLayoutParams(imageParams);

        //
        newCard.setX(x);
        newCard.setY(y);
        newCard.setCameraDistance(cameraDistance);
        newCard.setCardElevation(0);
        newCard.setRadius(0);
        newCard.setCardBackgroundColor(gameBoardContext.getResources().getColor(R.color.transparent));

        //GENERATE IDS
        int cardID = View.generateViewId();
        int imageID = View.generateViewId();
        newCard.setId(cardID);
        imageView.setId(imageID);

        //
        cardsHolder.addView(newCard);
        newCard.addView(imageView);

        //
        Picasso.with(gameBoardContext).load(R.drawable.back).noFade().into(imageView);

        return new ArrayList<>(Arrays.asList(cardID, imageID));
    }

    void createPointsDisplay(ArrayList<Object[]> displayList){
        // displayList Object[] order:
        // 0 (int)  :   points
        // 1 (int)  :   colorID
        // 2 (int)  :   imageID (-1 is no image)
        for(int i = 0; i < displayList.size(); i++){
            CardView pointsHolder = new CardView(gameBoardContext);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT);
            pointsHolder.setLayoutParams(layoutParams);
            pointsHolder.setCardElevation(0);
            pointsHolder.setRadius(3);
            pointsHolder.setCardBackgroundColor(gameBoardContext.getResources().getColor(R.color.transparent));

            //MAKE LINEAR LAYOUT

            //ADD THE IMAGE
            ImageView imageView = new ImageView(gameBoardContext);
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(30, 30);
            imageView.setLayoutParams(imageParams);

            //ADD THE POINTS TEXT

            //INFLATE THE VIEW

            //SET X AND Y
            pointsHolder.setX(0);//////!!!!!
            pointsHolder.setY(0);//////!!!!!

            //APPLY ANIMATION
        }

    }
}
