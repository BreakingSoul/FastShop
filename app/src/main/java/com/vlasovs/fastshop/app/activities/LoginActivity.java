package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.background.LoginResponse;
import com.vlasovs.fastshop.app.background.LoginTask;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements LoginResponse {

    private Button butLog;
    private TextInputLayout editEmail, editPass;
    private CheckBox saveUser;
    private LoadingDialog lD;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SAVED_USER_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        butLog = findViewById(R.id.buttonRegister);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPassword);
        saveUser = findViewById(R.id.saveUser);

        butLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
    }

    private void logIn() {
        if (isValidEmail() & isValidPass()) {

            String email = editEmail.getEditText().getText().toString();
            String pass = editPass.getEditText().getText().toString();

            LoginTask lT = new LoginTask();
            lT.delegate = this;
            lD = new LoadingDialog(this);
            lD.startLoadingDialog();
            lT.execute(email, pass);
        }
    }

    private boolean isValidEmail() {
        String emailInput = editEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            editEmail.setError("Field can't be empty");
            return false;
        }
        else {
            editEmail.setError(null);
            return true;
        }
    }

    private boolean isValidPass() {
        String emailInput = editPass.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            editPass.setError("Field can't be empty");
            return false;
        } else {
            editPass.setError(null);
            return true;
        }
    }

    @Override
    public void processFinish(String output) {
        if (output != null && TextUtils.isDigitsOnly(output)){
            int id = Integer.parseInt(output);
            if (saveUser.isChecked()){
                    saveUser(id);
            } else {
                    deleteUser();
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("fetchedID", id);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Email or password is incorrect!", Toast.LENGTH_LONG).show();
        }
        lD.dismissLoadingDialog();
    }

    private void deleteUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SAVED_USER_ID, -1);
   //     editor.putString("username", "Welcome!");

        editor.apply();

    }

    private void saveUser(int id) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SAVED_USER_ID, id);
  //      editor.putString("username", "")

        editor.apply();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
