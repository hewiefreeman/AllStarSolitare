package com.metagaming.allstarsolitare.gameBoard;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metagaming.allstarsolitare.R;


public class GameBoardActivity extends AppCompatActivity {

    //MAIN LAYOUT
    FrameLayout mainLayout;
    FrameLayout cardsHolder;
    TextView timeText;
    TextView scoreText;

    //GAME LOGIC
    Game game;

    //
    final private String TAG = "GAME_ACT";

    //
    Bundle restoreBundle;
    Boolean pausingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        Log.d(TAG, "onCreate()");

        //
        pausingActivity = false;

        //MAIN LAYOUT
        mainLayout = findViewById(R.id.game_board_frame_layout);
        cardsHolder = findViewById(R.id.game_board_cards_holder);
        timeText = findViewById(R.id.game_board_timer_text);
        scoreText = findViewById(R.id.game_board_score_text);

        //INITIALIZE GAME
        game = new Game();
        game.mainLayout = mainLayout;
        game.cardsHolder = cardsHolder;

        if(savedInstanceState == null){
            //FIST LOAD
            Log.d(TAG, "On-Create: NO SAVED INSTANCE!");
            game.gameType = getIntent().getIntExtra("gameType", 1);
            game.initGame(this, false);
            restoreBundle = null;

        }else if(savedInstanceState.size() == 0){
            //ERROR!! GO TO GAME SELECT SCREEN.
            Log.d(TAG, "On-Create: SAVED INSTANCE IS EMPTY?");

        }else if(!savedInstanceState.containsKey("fromSaveInstanceState")){
            //DO NOTHING SINCE ACTIVITY WAS ONLY PAUSED.
            Log.d(TAG, "On-Create: SAVED INSTANCE DOESN'T CONTAIN BOOLEAN");
            restoreBundle = new Bundle();

        }else if(savedInstanceState.containsKey("gameType")){
            //RESTORE GAME
            Log.d(TAG, "On-Create: USING SAVED INSTANCE TO RESTORE GAME");
            game.gameType = savedInstanceState.getInt("gameType");
            game.initGame(this, true);
            restoreBundle = savedInstanceState;
        }

        //SET INITIAL TIME AND SCORE
        timeText.setText("00:00");
        scoreText.setText("0");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //
        if(game.inSetup){
            //GAME WAS IN SETUP
            game.makeSetupTimer(((28-game.setupCardOn)*100)+1000).start();

        }else if(restoreBundle != null && restoreBundle.size() > 0){
            //RESTORED GAME
            Log.d(TAG, "RESTORING INSTANCE... ON START");
            onStartSetup();
            new RestoreGameState().restoreGame(game, restoreBundle, this);
            restoreBundle = new Bundle();

        }else if(restoreBundle != null && restoreBundle.size() == 0){
            //RETURNED FROM BACKGROUND
            game.gameBoardTimer.startTimer();
            Log.d(TAG, "RESTORE BUNDLE IS NOT NULL, BUT EMPTY!");

        }else if(restoreBundle == null){
            //INITIALIZING THE GAME
            if(!game.gameInited && !game.inSetup){
                onStartSetup();
                game.setUpGame();
            }
        }

        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(game.gameInited){
            game.gameBoardTimer.startTime = System.currentTimeMillis()-game.gameBoardTimer.timeElapsed;
            game.gameBoardTimer.startTimer();
        }
    }

    private void onStartSetup(){
        //Get Screen Size
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        if(screenWidth > screenHeight || screenWidth == screenHeight){
            Log.d(TAG, "SCREEN WITH > SCREEN HEIGHT");

            float tempCardHeight = size.y/6.5F;
            float cardRatio = (float) game.defaultCardWidth/game.defaultCardHeight;
            int tempCardWidth = Math.round(tempCardHeight*(cardRatio));
            screenWidth = tempCardWidth*7+(game.smallMargin*6)+(game.largeMargin*2);
            FrameLayout.LayoutParams screenParams = new FrameLayout.LayoutParams(screenWidth, FrameLayout.LayoutParams.MATCH_PARENT);
            game.cardsHolder.setLayoutParams(screenParams);
            game.cardsHolder.setX((size.x/2)-(screenWidth/2));
            game.screenWidth = screenWidth;
            game.screenHeight = size.y;

        }else{
            game.screenWidth = screenWidth;
            game.screenHeight = screenHeight;
        }
    }

    @Override
    protected void onPause() {
        if(game.inSetup){
            game.setUpTimer.cancel();
        }else{
            game.gameBoardTimer.pauseTimer();
        }

        pausingActivity = true;
        this.onSaveInstanceState(new Bundle());
        pausingActivity = false;

        Log.d(TAG, "PAUSED ACTIVITY");

        //
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new SaveGameInstance().saveGame(outState, game);

        if(!pausingActivity){
            bundle.putString("fromSaveInstanceState", "yes");
            Log.d(TAG, "ADDED BOOLEAN TO SAVED INSTANCE STATE");

        }else{
            Log.d(TAG, "SAVED INSTANCE STATE");
        }

        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "INSTANCE STATE SUPPOSEDLY WAS RESTORED.");
        super.onRestoreInstanceState(savedInstanceState);
    }
}