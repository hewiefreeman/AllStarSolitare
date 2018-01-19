package com.metagaming.allstarsolitare.gameBoard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private AnimationHelper animHelper;
    private TextView scoreTextLayout;

    void init(Context tempContext, FrameLayout tempLayout, FrameLayout tempMainLayout, TextView tempScoreTextLayout){
        gameBoardContext = tempContext;
        mainLayout = tempMainLayout;
        cardsHolder = tempLayout;
        scoreTextLayout = tempScoreTextLayout;
        animHelper = new AnimationHelper();
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

        int xOffset = 0;

        for(int i = 0; i < displayList.size(); i++){
            //ADD THE IMAGE IF NEEDED
            ImageView imageView = null;
            if((int) displayList.get(i)[2] >= 0){
                imageView = new ImageView(gameBoardContext);
                ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(Math.round(scoreTextLayout.getHeight() * 1.5F), Math.round(scoreTextLayout.getHeight() * 1.5F));
                imageView.setLayoutParams(imageParams);
                imageView.setX((-1) * (scoreTextLayout.getHeight() * 0.25F));
                imageView.setY((-1) * (scoreTextLayout.getHeight() * 0.25F));
            }

            //MAKE THE POINTS HOLDER AND DETERMINE IT'S WIDTH
            int holderWidth;
            if(imageView == null){
                holderWidth = scoreTextLayout.getHeight()*5;
            }else{
                holderWidth = scoreTextLayout.getHeight()*8;
            }
            final CardView pointsHolder = new CardView(gameBoardContext);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(holderWidth, scoreTextLayout.getHeight());
            pointsHolder.setLayoutParams(layoutParams);
            pointsHolder.setCardElevation(0);
            pointsHolder.setRadius(6);
            pointsHolder.setCardBackgroundColor(gameBoardContext.getResources().getColor((int) displayList.get(i)[1]));
            pointsHolder.setPaddingRelative(0, 0, scoreTextLayout.getPaddingEnd(), 0);

            //ADD THE POINTS TEXT
            TextView pointsText = new TextView(gameBoardContext);
            ViewGroup.LayoutParams pointsParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pointsText.setLayoutParams(pointsParams);
            pointsText.setText("+"+String.valueOf(displayList.get(i)[0]));
            pointsText.setTextSize(Math.round(scoreTextLayout.getTextSize()));
            pointsText.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);

            //INFLATE THE VIEWS
            mainLayout.addView(pointsHolder);
            if(imageView != null){
                pointsHolder.addView(imageView);
            }
            pointsHolder.addView(pointsText);

            //SET X AND Y
            pointsHolder.setX(scoreTextLayout.getX()-xOffset);
            pointsHolder.setY(scoreTextLayout.getY()-(scoreTextLayout.getHeight()+(scoreTextLayout.getHeight() * 0.25F)));

            //
            xOffset += pointsHolder.getWidth()+(scoreTextLayout.getHeight() * 0.25F);

            //APPLY ANIMATION
            ObjectAnimator yAnim = animHelper.makeAnimator(pointsHolder, 2000, View.Y, pointsHolder.getY(), pointsHolder.getY()-pointsHolder.getHeight());
            ObjectAnimator alphaAnim = animHelper.makeAnimator(pointsHolder, 2000, View.ALPHA, pointsHolder.getAlpha(), 0);

            //DELETE VIEW WHEN ANIMATION IS OVER
            yAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mainLayout.removeView(pointsHolder);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            yAnim.start();
            alphaAnim.start();
        }
    }
}
