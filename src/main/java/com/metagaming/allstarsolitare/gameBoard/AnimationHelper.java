package com.metagaming.allstarsolitare.gameBoard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Property;
import android.view.View;
import android.widget.ImageView;


class AnimationHelper {
    ObjectAnimator makeAnimator(Object view, long duration, Property<View, Float> animType, float from, float to){
        final String animTypeStr = animType.getName();
        ObjectAnimator transAnimation= ObjectAnimator.ofFloat(view, animTypeStr, from, to);
        transAnimation.setDuration(duration);
        //
        return transAnimation;
    }

    void flipCard(final CardView cardView, final ImageView imageView, final int resourceId, final Context context){
        final ObjectAnimator startAnim = makeAnimator(cardView, 300, View.ROTATION_Y, 0, 90);
        startAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startAnim.removeAllListeners();
                imageView.setImageDrawable(context.getDrawable(resourceId));
                ObjectAnimator endAnim =
                        makeAnimator(cardView, 300, View.ROTATION_Y, -90, 0);
                endAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
        }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        startAnim.start();
    }
}
