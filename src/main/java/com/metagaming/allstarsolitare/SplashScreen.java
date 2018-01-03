package com.metagaming.allstarsolitare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.metagaming.allstarsolitare.gameSelect.GameSelectActivity;
import com.metagaming.allstarsolitare.database.DBContract;
import com.metagaming.allstarsolitare.database.DBHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private SQLiteDatabase database;
    DBHelper dbHelper;

    private Boolean autoLogin = false;
    private String login = "";

    String googleName;
    GoogleSignInClient googleSignIn;

    ProgressBar loadingIndicator;

    //
    long splashScreenDelay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingIndicator = findViewById(R.id.splash_loading_indicator);

        //
        autoLogin = false;
        GoogleSignInAccount account;

        //CHECK IF YOU'VE LOGGED IN WITH GOOGLE BEFORE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignIn = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            login = account.getEmail();
            googleName = account.getDisplayName();
            autoLogin = true;
        }else{
            //IF YOU HAVEN'T LOGGED IN WITH GOOGLE:
            //SET UP DATABASE
            dbHelper = new DBHelper(this);
            database = dbHelper.getReadableDatabase();

            //
            Cursor accounts = getAllAccounts();
            accounts.moveToFirst();

            //LOOK THROUGH ACCOUNTS FOR GUEST PLAYER
            for(int i = 0; i < accounts.getCount(); i++){
                if(accounts.getString(accounts.getColumnIndex(DBContract.AccountsEntry.COLUMN_USER_NAME)).equals("guestPlayer")){
                    //CHECK IF YOU'VE LOGGED IN WITH GUEST LAST
                    if(accounts.getString(accounts.getColumnIndex(DBContract.AccountsEntry.COLUMN_AUTOLOG)).equals("true")){
                        //AUTO-LOGIN WITH GUEST ACCOUNT
                        autoLogin = true;
                        login = "guestPlayer";

                        //
                        break;
                    }
                }
            }
        }
        Timer closeSplash = new Timer();
        closeSplash.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent;

                //CHANGE INTENT IF USING AUTO-LOGIN
                if(autoLogin){
                    if(!login.equals("guestPlayer")){
                        //LOG IN WITH GOOGLE
                        intent = new Intent(SplashScreen.this, GameSelectActivity.class);
                        intent.putExtra("login", googleName);
                        intent.putExtra("autoLog", true);

                    }else{
                        //GUEST PLAYER: GO DIRECTLY TO GAME SELECT SCREEN
                        intent = new Intent(SplashScreen.this, GameSelectActivity.class);
                        intent.putExtra("login", login);
                        intent.putExtra("autoLog", true);
                    }
                }else{
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }

                startActivity(intent);
            }
        }, splashScreenDelay);
    }

    private Cursor getAllAccounts() {
        return database.query(DBContract.AccountsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBContract.AccountsEntry._ID);
    }
}