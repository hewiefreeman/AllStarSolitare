package com.metagaming.allstarsolitare.gameBoard;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import com.metagaming.allstarsolitare.R;


public class GameBoardActivity extends AppCompatActivity {

    //MAIN LAYOUT
    FrameLayout mainLayout;
    FrameLayout cardsHolder;

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

        //INITIALIZE GAME
        game = new Game();
        game.mainLayout = mainLayout;
        game.cardsHolder = cardsHolder;

        if(savedInstanceState == null){
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
            Log.d(TAG, "On-Create: USING SAVED INSTANCE TO RESTORE GAME");
            game.gameType = savedInstanceState.getInt("gameType");
            game.initGame(this, true);
            restoreBundle = savedInstanceState;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //
        if(game.inSetup){
            //restart setup timer
            game.makeSetupTimer(((28-game.setupCardOn)*100)+1000).start();
        }else if(restoreBundle != null && restoreBundle.size() > 0){
            Log.d(TAG, "RESTORING INSTANCE... ON START");
            onStartSetup();
            new RestoreGameState().restoreGame(game, restoreBundle, this);
            restoreBundle = new Bundle();

        }else if(restoreBundle != null && restoreBundle.size() == 0){
            //do nothing
            Log.d(TAG, "RESTORE BUNDLE IS NOT NULL, BUT EMPTY!");
        }else if(restoreBundle == null){
            if(!game.gameInited && !game.inSetup){
                onStartSetup();
                game.setUpGame();
            }
        }

        Log.d(TAG, "onStart()");
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