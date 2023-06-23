package com.example.taskmanager.user_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.R;
import com.example.taskmanager.activities.NavigationInterface;
import com.example.taskmanager.db.user.User;
import com.example.taskmanager.db.user.UserViewModel;
import com.example.taskmanager.utils.PasswordEncryption;

public class RegisterFragment extends Fragment {
    private UserViewModel userViewModel;
    private EditText usernameEditText, passwordEditText;
    private NavigationInterface navigationInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationInterface) {
            navigationInterface = (NavigationInterface) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationInterface = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        usernameEditText = view.findViewById(R.id.userEdit);
        passwordEditText = view.findViewById(R.id.passEdit);
        Button registerButton = view.findViewById(R.id.registerBtn);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        registerButton.setOnClickListener(v -> validateFields());
    }

    private void registerUser(String username, String password) {
        String encryptedPassword = PasswordEncryption.encryptPassword(password);
        if (encryptedPassword != null) {
            User newUser = new User(0, username, encryptedPassword);
            userViewModel.registerUser(newUser, success -> {
                if (success) {
                    navigationInterface.navigateToPrincipalActivity(username);
                } else {
                    showMessage("Lo siento, pero ese usuario ya está registrado");
                }
            });
        } else {
            showMessage("Error al encriptar la contraseña");
        }
    }

    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Los campos Usuario y Contraseña son obligatorios");
        } else {
            registerUser(username, password);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
