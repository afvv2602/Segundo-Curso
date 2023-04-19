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


    private EditText userEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private RadioGroup genreRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private CheckBox conditionsCheckBox;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        userEditText = findViewById(R.id.userEditText);
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
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        int checkedRadioButtonId = genreRadioGroup.getCheckedRadioButtonId();

        if (user.isEmpty()) {
            showMessage("El campo User no puede estar vacío");
            return false;
        } else if (email.isEmpty()) {
            showMessage("El campo Email no puede estar vacío");
            return false;
        } else if (phone.isEmpty()) {
            showMessage("El campo Phone no puede estar vacío");
            return false;
        } else if (checkedRadioButtonId == -1) {
            showMessage("Debes seleccionar un género");
            return false;
        }

        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void register(){
        if (validateFields()) {
            // Obtén los valores de los campos de registro
            String user = userEditText.getText().toString();
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

            // Validar los campos si es necesario (por ejemplo, que no estén vacíos)

            Intent intent = new Intent(RegisterActivity.this, RegisterDetailsActivity.class);
            // Pasa los valores a la siguiente actividad
            intent.putExtra("user", user);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("genre", genre);
            intent.putExtra("conditionsAccepted", conditionsAccepted);
            startActivity(intent);
        }
    }
}