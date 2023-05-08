package com.example.libreriaroom.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.libreriaroom.R;
import com.example.libreriaroom.db.user.UserViewModel;

public class LoginFragment extends Fragment {
    private UserViewModel userViewModel;
    private EditText usernameEditText,passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        userViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // El inicio de sesión fue exitoso, navega a la pantalla principal.
            } else {
                // Muestra un mensaje de error.
            }
        });

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> validateFields());
        registerButton.setOnClickListener(v -> registerFragment());
    }

    private void registerFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.LoginFragment, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }

    private void loginUser(String username, String password) {
        userViewModel.loginUser(username, password);
    }

    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty()) {
            showMessage("El campo User no puede estar vacio");
        } else if (password.isEmpty()) {
            showMessage("El campo password no puede estar vacio");
        } else{
            loginUser(username, password);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}