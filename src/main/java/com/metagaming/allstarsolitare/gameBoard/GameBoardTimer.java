package com.metagaming.allstarsolitare.gameBoard;

import android.annotation.SuppressLint;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class GameBoardTimer {

    long startTime;
    long timeElapsed;
    TextView timerText;
    Timer theTimer;

    void initTimer(FrameLayout mainLayout){
        timerText = mainLayout.findViewById(R.id.game_board_timer_text);
        startTime = System.currentTimeMillis();
        startTimer();
    }

    void startTimer(){
        theTimer = new Timer();
        theTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                synchronized(this){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerText.setText(getTime());
                        }
                    });
                }
            }
        },0,1000);
    }

    void pauseTimer(){
        theTimer.cancel();
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime() {
        long nowTime = System.currentTimeMillis();
        timeElapsed = nowTime - startTime;
        Date date = new Date(timeElapsed);
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }
}
