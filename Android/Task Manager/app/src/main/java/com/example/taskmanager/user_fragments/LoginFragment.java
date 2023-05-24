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
import com.example.taskmanager.db.user.UserViewModel;

public class LoginFragment extends Fragment {
    private UserViewModel userViewModel;
    private EditText usernameEditText,passwordEditText;

    // Referencia al activity para cambiar de fragments
    private NavigationInterface navigationInterface;

    // Se infla la vista del fragmento de inicio de sesion
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // Se guarda una referencia al activity cuando el fragmento se añade
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationInterface) {
            navigationInterface = (NavigationInterface) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    // Se borra la referencia al activity
    @Override
    public void onDetach() {
        super.onDetach();
        navigationInterface = null;
    }

    // Metodo para configurar la vista una vez que se ha creado
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    // Configura los elementos de la vista
    private void setUpViews(View view) {
        usernameEditText = view.findViewById(R.id.userEdit);
        passwordEditText = view.findViewById(R.id.passEdit);
        Button loginButton = view.findViewById(R.id.loginBtn);
        Button registerButton = view.findViewById(R.id.registerBtn);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        loginButton.setOnClickListener(v -> validateFields());
        registerButton.setOnClickListener(v -> navigationInterface.navigateToRegister());
    }

    // Inicia sesion del usuario
    private void loginUser(String username, String password) {
        userViewModel.loginUser(username, password);
        userViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                navigationInterface.navigateToPrincipalActivity(username);
            } else {
                showMessage("Algo ha ido mal por favor intentelo de nuevo");
            }
        });
    }

    // Valida que los campos no esten vacios
    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Los campos Usuario y Contraseña son obligatorios");
        } else{
            loginUser(username, password);
        }
    }

    // Muestra un mensaje en pantalla
    private void showMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
