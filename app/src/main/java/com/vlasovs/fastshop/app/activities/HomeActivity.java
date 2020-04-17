package com.vlasovs.fastshop.app.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.classes.User;

public class HomeActivity extends AppCompatActivity {

    private TextView loggedUserTW;
    private Button butReg, butLog, butSignOut;
    private User user;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SAVED_USER_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        butReg = findViewById(R.id.butReg);
        butLog = findViewById(R.id.butLog);
        butSignOut = findViewById(R.id.butSignOut);
        loggedUserTW = findViewById((R.id.textUser));

        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistration();
            }
        });

        butLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogIn();
            }
        });

        butSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });

        loadUser();

    }

    private void openConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Are you really want to log out?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    loggedUserTW.setText("Sign in please :D");
                    user.setID(-1);
                    saveUser();
                    butSignOut.setVisibility(View.GONE);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SAVED_USER_ID, user.getID());

        editor.apply();

    }

    private void loadUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        user = new User(sharedPreferences.getInt(SAVED_USER_ID, -1));

        if (user.getID() != -1) {
            butSignOut.setVisibility(View.VISIBLE);
            loggedUserTW.setText("Logged in user: " + user.getID());
        } else {
            butSignOut.setVisibility(View.GONE);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            if (resultCode == RESULT_OK){
                int userID = data.getIntExtra("fetchedID", 0);
                user = new User(userID);
                loggedUserTW.setText("Logged in user: " + user.getID());
                butSignOut.setVisibility(View.VISIBLE);
            }
        }
    }

    private void openLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    private void openRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
