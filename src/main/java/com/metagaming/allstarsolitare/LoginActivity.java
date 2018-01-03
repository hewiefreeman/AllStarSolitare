package com.metagaming.allstarsolitare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.metagaming.allstarsolitare.gameSelect.GameSelectActivity;
import com.metagaming.allstarsolitare.database.DBContract.AccountsEntry;
import com.metagaming.allstarsolitare.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    CardView guestLoginBtn;
    CardView googleLoginBtn;

    ConstraintLayout constraintLayout;

    DBHelper dbHelper;
    private SQLiteDatabase database;

    int signInCode = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        guestLoginBtn = findViewById(R.id.login_guest_card);
        googleLoginBtn = findViewById(R.id.login_google_card);
        constraintLayout = findViewById(R.id.login_constraint_layout);

        //
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        //GUEST BUTTON CLICKED
        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //just go to game select screen
                Intent intent = new Intent(LoginActivity.this, GameSelectActivity.class);
                intent.putExtra("login", "guestPlayer");
                intent.putExtra("autoLog", false);
                startActivity(intent);
            }
        });

        //GOOGLE BUTTON CLICKED
        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN GOOGLE LOGIN WINDOW
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient signInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                Intent intent = signInClient.getSignInIntent();
                startActivityForResult(intent, signInCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == signInCode) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> googleSignInAccountTask){
        GoogleSignInAccount account;
        Boolean accountMade = false;
        try{
            account = googleSignInAccountTask.getResult(ApiException.class);

            Cursor loginInfo = checkForAccount(account.getDisplayName());

            loginInfo.moveToFirst();
            for(int i = 0; i < loginInfo.getCount(); i++){
                if(loginInfo.getString(loginInfo.getColumnIndex(AccountsEntry.COLUMN_USER_NAME)).equals(account.getDisplayName())){
                    //Account for this login already made
                    accountMade = true;

                    //
                    break;
                }
                loginInfo.moveToNext();
            }

            if(!accountMade){
                //INSERT NEW USER TO ACCOUNTS
                final String INSERT_NEW_USER_QUERY = "INSERT INTO " + AccountsEntry.TABLE_NAME +
                        " (" + AccountsEntry.COLUMN_USER_NAME + ", " + AccountsEntry.COLUMN_AUTOLOG +
                        ", " + AccountsEntry.COLUMN_CURRENCY + ", " + AccountsEntry.COLUMN_LEVEL + ", " +
                        AccountsEntry.COLUMN_EXP + ") VALUES (\"" + account.getDisplayName() + "\", \"false\", 500, 1, 0);";

                //
                database.execSQL(INSERT_NEW_USER_QUERY);

                Log.d("LOGIN", "INSERTED '"+account.getDisplayName()+"' INTO DATABASE");
            }else{
                Log.d("LOGIN", "ALREADY HAVE INSERTED '"+account.getDisplayName()+"' INTO DATABASE!");
            }

            //GO TO GAME SELECT SCREEN
            Intent intent = new Intent(LoginActivity.this, GameSelectActivity.class);
            intent.putExtra("login", account.getDisplayName());
            intent.putExtra("autoLog", false);
            startActivity(intent);

        }catch(ApiException e){
            e.printStackTrace();

            //
            Snackbar snackbar = Snackbar.make(constraintLayout, R.string.google_login_error, Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }

    private Cursor checkForAccount(String accountName) {
        return database.query(AccountsEntry.TABLE_NAME,
                null,
                AccountsEntry.COLUMN_USER_NAME+" = \""+accountName+"\"",
                null,
                null,
                null,
                null,
                null);
    }

    //PREVENT BACK BUTTON
    @Override
    public void onBackPressed() {
        //Do nothing
    }
}