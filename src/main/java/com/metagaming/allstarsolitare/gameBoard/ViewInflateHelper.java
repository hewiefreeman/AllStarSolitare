package com.metagaming.allstarsolitare.gameBoard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.icu.util.MeasureUnit;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
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
            Drawable pointsDisplayBG = gameBoardContext.getDrawable(R.drawable.score_bg_shape_good);
            String pointsPrefix = "+";
            int textBaseWidth;
            int yOffset = 0;
            if((int) displayList.get(i)[0] < 100){
                textBaseWidth = Math.round(scoreTextLayout.getHeight()*1.4F);
            }else{
                textBaseWidth = Math.round(scoreTextLayout.getHeight()*1.6F);
            }

            int holderWidth = textBaseWidth;
            if((int) displayList.get(i)[0] < 0){
                pointsDisplayBG = gameBoardContext.getDrawable(R.drawable.score_bg_shape_bad);
                pointsPrefix = "-";
            }

            //ADD A BONUS IMAGE
            ImageView imageView = null;
            if((int) displayList.get(i)[2] >= 0){
                imageView = new ImageView(gameBoardContext);
                ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(Math.round(scoreTextLayout.getHeight() * 1.5F), Math.round(scoreTextLayout.getHeight() * 1.5F));
                imageView.setLayoutParams(imageParams);
                imageView.setX(0);
                imageView.setY(0);
                imageView.setImageDrawable(gameBoardContext.getDrawable((int) displayList.get(i)[2]));

                //
                holderWidth += Math.round(scoreTextLayout.getHeight()*1.5F);
                yOffset = Math.round(scoreTextLayout.getHeight() * 0.4F);
            }

            //MAKE THE HOLDER
            final FrameLayout pointsHolder = new FrameLayout(gameBoardContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(holderWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
            pointsHolder.setLayoutParams(layoutParams);
            //pointsHolder.setBackgroundColor(gameBoardContext.getResources().getColor(R.color.text_white));


            //ADD THE POINTS TEXT
            TextView pointsText = new TextView(gameBoardContext);
            ViewGroup.LayoutParams pointsParams = new ViewGroup.LayoutParams(textBaseWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            pointsText.setLayoutParams(pointsParams);
            pointsText.setText(pointsPrefix+String.valueOf(displayList.get(i)[0]));
            pointsText.setTextColor(gameBoardContext.getResources().getColor(R.color.text_white));
            pointsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.round(scoreTextLayout.getTextSize()));
            pointsText.setBackground(pointsDisplayBG);
            pointsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            //INFLATE THE VIEWS
            mainLayout.addView(pointsHolder);
            if(imageView != null){
                pointsHolder.addView(imageView);
            }
            pointsHolder.addView(pointsText);

            //SET Xs/Ys
            pointsHolder.setX(((scoreTextLayout.getX()+scoreTextLayout.getWidth())-holderWidth)-xOffset);
            pointsHolder.setY((scoreTextLayout.getY()-(scoreTextLayout.getHeight()+(scoreTextLayout.getHeight() * 0.25F)))-yOffset);
            pointsText.setX(holderWidth-textBaseWidth);
            if(imageView != null){
                pointsText.setY(Math.round(scoreTextLayout.getHeight() * 0.4F));
            }

            //
            xOffset += holderWidth+(scoreTextLayout.getHeight() * 0.25F);

            //APPLY ANIMATION
            ObjectAnimator yAnim = animHelper.makeAnimator(pointsHolder, 4000, View.Y,
                    (scoreTextLayout.getY()-(scoreTextLayout.getHeight()+(scoreTextLayout.getHeight() * 0.25F)))-yOffset,
                    ((scoreTextLayout.getY()-(scoreTextLayout.getHeight()+(scoreTextLayout.getHeight() * 0.25F)))-yOffset)-(scoreTextLayout.getHeight()*1.5F));
            ObjectAnimator alphaAnim = animHelper.makeAnimator(pointsHolder, 4000, View.ALPHA, pointsHolder.getAlpha(), 0);

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
