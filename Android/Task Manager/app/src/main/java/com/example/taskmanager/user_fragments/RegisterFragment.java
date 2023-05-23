package com.example.taskmanager.user_fragments;

import android.content.Context;
import android.os.Bundle;
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

public class RegisterFragment extends Fragment {
    UserViewModel userViewModel;
    EditText usernameEditText,passwordEditText;
    private NavigationInterface navigationInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) { super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        usernameEditText = view.findViewById(R.id.userEdit);
        passwordEditText = view.findViewById(R.id.passEdit);
        Button registerButton = view.findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(v -> validateFields());
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

    private void registerUser(String username, String password) {
        User newUser = new User(0, username, password);
        userViewModel.registerUser(newUser,success -> {
            if (success) {
                showMessage("Registro con exito!");
                navigationInterface.navigateToPrincipalActivity(username);
            } else {
                showMessage("Lo siento, pero ese usuario ya esta registrado");
            }
        });
    }

    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Los campos Usuario y Contraseña son obligatorios");
        } else{
            registerUser(username, password);
        }
    }

    // Hay que forzar que el toast use el hilo principal de la UI para que funcione la aplicacion.
    private void showMessage(String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
    }
}
