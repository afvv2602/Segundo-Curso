package com.example.libreriaroom.user_fragments;

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

import com.example.libreriaroom.R;
import com.example.libreriaroom.activities.NavigationInterface;
import com.example.libreriaroom.db.user.UserViewModel;

public class LoginFragment extends Fragment {
    private UserViewModel userViewModel;
    private EditText usernameEditText,passwordEditText;

    //Con esto guardamos una referencia del activity para luego poder cambiar los fragments
    private NavigationInterface navigationInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        loginButton.setOnClickListener(v -> validateFields());
        registerButton.setOnClickListener(v -> navigationInterface.navigateToRegister());
    }

    private void loginUser(String username, String password) {
        userViewModel.loginUser(username, password);
        userViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                showMessage("Login con exito!.");
                navigationInterface.navigateToPrincipalActivity(username);
            } else {
                showMessage("Algo ha ido mal por favor intentelo de nuevo");
            }
        });
    }

    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Los campos Usuario y Contraseña son obligatorios");
        } else{
            loginUser(username, password);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}