package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseController db;
    EditText userEditText,passwordEditText, emailEditText, phoneEditText;
    RadioGroup genderRadioGroup;
    Button editBtn,dltBtn;
    TextView generoTV;
    String username;

    private boolean editar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        db = new DatabaseController(this);

        // Obtener el nombre de usuario del Intent
        username = getIntent().getStringExtra("username");

        // Vincular elementos de la vista
        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        generoTV = findViewById(R.id.generoTV);
        editBtn = findViewById(R.id.editBtn);
        dltBtn = findViewById(R.id.eliminarBtn);
        // Cargar datos de usuario
        cargarDatos();
        //Desactivar los editText
        activarEditar(false);
        editBtn.setOnClickListener(v -> funcionesBoton());
        dltBtn.setOnClickListener(v -> borrarUsuario());
    }

    private void cargarDatos() {
        userEditText.setText(username);
        passwordEditText.setText(db.getPassword(username));
        emailEditText.setText(db.getEmail(username));
        phoneEditText.setText(db.getPhone(username));
        String genre = db.getUserGenre(username);
        RadioButton masculino = findViewById(R.id.masculino);
        RadioButton femenino = findViewById(R.id.femenino);

        if ("masculino".equals(genre)) {
            masculino.setChecked(true);
        } else if ("femenino".equals(genre)) {
            femenino.setChecked(true);
        }
    }

    private void activarEditar(boolean editable) {
        passwordEditText.setEnabled(editable);
        emailEditText.setEnabled(editable);
        phoneEditText.setEnabled(editable);
    }

    private void funcionesBoton() {
        if (!editar) {
            activarEditar(true);
            editBtn.setText("Guardar");
        } else {
            // Actualizar los datos en la base de datos
            String newPassword = passwordEditText.getText().toString();
            String newEmail = emailEditText.getText().toString();
            String newPhone = phoneEditText.getText().toString();
            int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
            String newGenre = (selectedRadioButtonId == R.id.masculino) ? "masculino" : "femenino";
            db.updateUser(username, newPassword, newEmail, newGenre,newPhone);
            activarEditar(false);
            editBtn.setText("Editar Usuario");
            finish();
        }
        editar = !editar;
    }

    private void borrarUsuario() {
        db.deleteUser(username);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
