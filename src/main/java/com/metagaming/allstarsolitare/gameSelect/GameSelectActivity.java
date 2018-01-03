package com.metagaming.allstarsolitare.gameSelect;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.metagaming.allstarsolitare.gameBoard.GameBoardActivity;
import com.metagaming.allstarsolitare.LoginActivity;
import com.metagaming.allstarsolitare.R;
import com.metagaming.allstarsolitare.database.DBContract.AccountsEntry;
import com.metagaming.allstarsolitare.database.DBHelper;

import java.net.URL;

public class GameSelectActivity extends AppCompatActivity implements LogOutDialog.NoticeDialogListener, LoaderManager.LoaderCallbacks {

    //
    private SQLiteDatabase database;
    DBHelper dbHelper;

    //
    private String login;

    //
    Snackbar errorSnackBar;

    //
    GameSelectAsyncImageLoader imageLoader;

    //
    GoogleSignInClient googleSignIn;

    //
    AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    private TextView login_avatar_letter;
    private TextView login_username;
    private TextView login_level;
    private TextView login_currency;
    private DialogFragment alertDialogFragment;
    CoordinatorLayout mainLayout;
    ImageView headerImage;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        //VIEWS
        mainLayout = findViewById(R.id.game_select_coordinator_layout);
        headerImage = findViewById(R.id.game_select_header_image);
        appBar = findViewById(R.id.game_select_appbar);
        collapsingToolbar = findViewById(R.id.game_select_collapsing_toolbar);
        nestedScrollView = findViewById(R.id.game_select_scroll_view);
        login_avatar_letter = findViewById(R.id.game_select_login_avatar_letter);
        login_username = findViewById(R.id.game_select_login_name);
        login_level = findViewById(R.id.game_select_login_level);
        login_currency = findViewById(R.id.game_select_currency);
        toolbar = findViewById(R.id.game_select_toolbar);
        adView = findViewById(R.id.game_select_ad_view);
        alertDialogFragment = new LogOutDialog();

        //DATABASE
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignIn = GoogleSignIn.getClient(this, gso);

        //GET INTENT INFO
        Intent intent = getIntent();
        Boolean autoLog = intent.getBooleanExtra("autoLog", false);
        login = intent.getStringExtra("login");
        Log.d("GAME_SELECT", "LOGGED IN AS: " + login);

        //CHANGE AUTO-LOGIN TO TRUE
        if(!autoLog){
            final String updateUserQuery = "UPDATE "+ AccountsEntry.TABLE_NAME +
                    " SET " + AccountsEntry.COLUMN_AUTOLOG +" = \"true\" WHERE " + AccountsEntry.COLUMN_USER_NAME+" = \"" +
                    login + "\";";
            database.execSQL(updateUserQuery);
        }

        //SET-UP LOGIN INFO
        getYourAccount();

        //DISPLAY TITLE ONLY WHEN COLLAPSED
        //SET UP TOOLBAR
        setSupportActionBar(toolbar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= scrollRange/2) {
                    final String gameScreenTitle = getResources().getString(R.string.title_activity_game_select);
                    collapsingToolbar.setTitle(gameScreenTitle);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        //LOAD UPDATED HEADER IMAGE OVER PLACEHOLDER
        //THIS IS INTENDED TO PROMOTE UPDATE FEATURES IN A VISUAL FASHION
        //WILL EVENTUALLY HAVE A TIMER THAT LOOPS THROUGH DIFFERENT IMAGES IF PATCH HAS
        //   MORE THAN ONE FEATURE IMAGE
        imageLoader = new GameSelectAsyncImageLoader();
        URL imageURL = imageLoader.init();
        if(imageURL != null){
            new GameSelectAsyncImageLoader.AsyncImageLoader(new GameSelectAsyncImageLoader.OnImageLoad() {
                @Override
                public void onImageLoadCompleted(Bitmap result) {
                    headerImage.setImageBitmap(result);
                }

                @Override
                public void onImageLoadError() {
                    errorLoadingHeaderImage();
                }
            }).execute(imageURL);
        }else{
            errorLoadingHeaderImage();
        }

        //MAKE BOTTOM PADDING FOR AD
        int adMargin = 10;
        nestedScrollView.setPadding(0, 0, 0, adView.getAdSize().getHeightInPixels(this)+(dpToPx(adMargin)*2));

        //DISPLAY AD
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    //Helpful function from https://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_select_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.game_select_menu_log_out){
            verifyLogOut();
        }

        //
        return false;
    }

    //
    public void setUpLoginInfo(Cursor loginInfo){
        loginInfo.moveToFirst();

        //TOP HALF
        if(login.equals("guestPlayer")){
            login_username.setText(R.string.guest);
            login_avatar_letter.setText(getResources().getString(R.string.g_for_guest));
        }else{
            login_username.setText(login);
            login_avatar_letter.setText(login.substring(0, 1));
        }

        String currency = loginInfo.getInt(loginInfo.getColumnIndex(AccountsEntry.COLUMN_CURRENCY))+"";
        String level = loginInfo.getInt(loginInfo.getColumnIndex(AccountsEntry.COLUMN_LEVEL))+"";

        //BOTTOM HALF
        login_currency.setText(currency);
        login_level.setText(level);
    }

    private void getYourAccount() {
        Bundle loaderBundle = new Bundle();
        loaderBundle.putString("userName", login);
        getSupportLoaderManager().initLoader(1, loaderBundle, this);
        /*return database.query(AccountsEntry.TABLE_NAME,
                null,
                AccountsEntry.COLUMN_USER_NAME+" = \""+login+"\"",
                null,
                null,
                null,
                null,
                null);*/
    }

    public void buttonClicked(View v){
        if(v.getId() == R.id.game_select_button_play_single){
            //PLAY SINGLE-DRAW
            Intent intent = new Intent(this, GameBoardActivity.class);
            intent.putExtra("gameType", 1);
            startActivity(intent);
        }
    }

    public void verifyLogOut(){
        alertDialogFragment.show(getSupportFragmentManager(), "LOGOUT_DLG");
    }

    //ALERT DIALOG CALLBACKS
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        logAndSignOut();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        logAndSignOut();
    }

    //
    public void logAndSignOut(){
        if(login.equals("guestPlayer")){
            logOut();
        }else{
            signOut();
        }
    }

    //LOG OUT FUNCTION
    public void logOut(){
        //CHANGE AUTO-LOG TO FALSE
        final String updateUserQuery = "UPDATE "+ AccountsEntry.TABLE_NAME +
                " SET " + AccountsEntry.COLUMN_AUTOLOG + " = \"false\" WHERE "
                + AccountsEntry.COLUMN_USER_NAME + " = \""+login+"\";";
        database.execSQL(updateUserQuery);

        //GO TO LOGIN SCREEN
        Intent intent = new Intent(GameSelectActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        googleSignIn.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                logOut();
            }
        });
    }

    void errorLoadingHeaderImage(){
        errorSnackBar = Snackbar.make(mainLayout, R.string.game_select_error_header_image_load, Snackbar.LENGTH_SHORT);
        errorSnackBar.show();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AccountsEntry.URI.buildUpon().appendPath(args.getString("userName")).build(), null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object cursor) {
        setUpLoginInfo((Cursor) cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}