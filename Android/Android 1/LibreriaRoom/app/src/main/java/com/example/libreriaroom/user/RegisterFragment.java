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
import com.example.libreriaroom.db.user.User;
import com.example.libreriaroom.db.user.UserViewModel;

public class RegisterFragment extends Fragment {
    UserViewModel userViewModel;
    EditText usernameEditText,passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModel.UserViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        userViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
            } else {
                showMessage("Ha ocurrido un error al intentar registrar el usuario pruebe de nuevo");
            }
        });

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> validateFields());
    }

    private void registerUser(String username, String password) {
        User newUser = new User(0, username, password);
        userViewModel.registerUser(newUser);

        getParentFragmentManager().popBackStack();
    }

    private void validateFields() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty()) {
            showMessage("El campo User no puede estar vacio");
        } else if (password.isEmpty()) {
            showMessage("El campo password no puede estar vacio");
        } else{
            registerUser(username, password);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
