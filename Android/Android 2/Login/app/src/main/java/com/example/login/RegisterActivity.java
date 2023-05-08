package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {


    private EditText userEditText,passwordEditText,emailEditText,phoneEditText;
    private RadioGroup genreRadioGroup;
    private RadioButton maleRadioButton,femaleRadioButton;
    private CheckBox conditionsCheckBox;
    private Button registerButton;

    //Database controller
    DatabaseController db = new DatabaseController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        genreRadioGroup = findViewById(R.id.genreRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        conditionsCheckBox = findViewById(R.id.conditionsCheckBox);
        registerButton = findViewById(R.id.registerButton);
        Button themeButton = findViewById(R.id.themeSwitchBut);

        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));
        registerButton.setOnClickListener(v -> register());
    }

    private boolean validateFields() {
        String user = userEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        int checkedRadioButtonId = genreRadioGroup.getCheckedRadioButtonId();
        if (user.isEmpty()) {
            UtilsController.showMessage(this,"El campo User no puede estar vacio");
            return false;
        }
        else if (password.isEmpty()) {
            UtilsController.showMessage(this,"El campo Password no puede estar vacio");
            return false;
        }
        else if (email.isEmpty()) {
            UtilsController.showMessage(this,"El campo Email no puede estar vacio");
            return false;
        }
        else if (phone.isEmpty()) {
            UtilsController.showMessage(this,"El campo Phone no puede estar vacio");
            return false;
        }
        if (checkedRadioButtonId == -1) {
            UtilsController.showMessage(this,"Debes seleccionar un genero");
            return false;
        }
        if (!conditionsCheckBox.isChecked()){
            UtilsController.showMessage(this,"Debes aceptar las condiciones de uso");
            return false;
        }
        return true;
    }

    private void register(){
        if (validateFields()) {
            String user = userEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String genre = null;
            int checkedRadioButtonId = genreRadioGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.maleRadioButton) {
                genre = "male";
            } else if (checkedRadioButtonId == R.id.femaleRadioButton) {
                genre = "female";
            }
            boolean conditionsAccepted = conditionsCheckBox.isChecked();
            Intent intent = new Intent(this,RegisterDetailsActivity.class);
            if (conditionsAccepted) {
                db.registerUser(user,password,email,genre,phone);
            }
            intent.putExtra("username", user);
            startActivity(intent);
            finish();
        }
    }

}