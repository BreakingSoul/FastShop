package com.vlasovs.fastshop.app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.background.RegisterTask;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

public class RegistrationActivity extends AppCompatActivity {

    private Button butReg;
    private TextInputLayout editEmail, editPass, editName, editSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);


        butReg = findViewById(R.id.buttonRegister);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);


        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    /*    editEmail.getEditText().addTextChangedListener(validator);
        editPass.getEditText().addTextChangedListener(validator);
        editName.getEditText().addTextChangedListener(validator);
        editSurname.getEditText().addTextChangedListener(validator);*/

    }

    private void registerUser() {

        if (isValidEmail() & isValidPass() & isValidName()  & isValidSurname()) {

            String email = editEmail.getEditText().getText().toString();
            String pass = editPass.getEditText().getText().toString();
            String name = editName.getEditText().getText().toString();
            String surname = editSurname.getEditText().getText().toString();

            RegisterTask rT = new RegisterTask(this);
            rT.execute(email, pass, name, surname);
        }

    }

   /* private TextWatcher validator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //    String valEmail = editEmail.getEditText().getText().toString().trim();
        //    String valPass = editPass.getEditText().getText().toString().trim();
        //    String valName = editName.getEditText().getText().toString().trim();
        //    String valSur = editSurname.getEditText().getText().toString().trim();
        //    boolean validEmail = isValidEmail();
       //     boolean validPass = (editPass.getEditText().getText().toString().trim().length() > 7);
         //   boolean validName = (edit.getEditText().getText().toString().trim().isEmpty());
        //    boolean validSurname = (editPass.getEditText().getText().toString().trim().isEmpty());



        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };*/

    private boolean isValidEmail() {
        String emailInput = editEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            editEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            editEmail.setError("Email is not valid");
            return false;
        }
        else {
            editEmail.setError(null);
            return true;
        }
    }

    private boolean isValidPass() {
        String emailInput = editPass.getEditText().getText().toString().trim();

        if (emailInput.length() < 8){
            editPass.setError("Field should be at least 8 characters long");
            return false;
        } else {
            editPass.setError(null);
            return true;
        }
    }

    private boolean isValidName() {
        String emailInput = editName.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            editName.setError("Field can't be empty");
            return false;
        } else {
            editName.setError(null);
            return true;
        }
    }

    private boolean isValidSurname() {
        String emailInput = editSurname.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            editSurname.setError("Field can't be empty");
            return false;
        } else {
            editSurname.setError(null);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
