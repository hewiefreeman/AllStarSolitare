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

class CardHelper {

    float cameraDistance;
    int cardWidth;
    int cardHeight;
    private Context gameBoardContext;
    private FrameLayout gameBoardFrameLayout;

    void init(Context tempContext, FrameLayout tempLayout){
        gameBoardContext = tempContext;
        gameBoardFrameLayout = tempLayout;
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
        gameBoardFrameLayout.addView(newCard);
        newCard.addView(imageView);

        //
        Picasso.with(gameBoardContext).load(R.drawable.back).noFade().into(imageView);

        return new ArrayList<>(Arrays.asList(cardID, imageID));
    }
}
