package com.example.libreriaroom.db.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.libreriaroom.db.AppDatabase;

// El ViewModel es el que se encarga de pasarle la informacion a la UI, se encarga de hacer
// de puente entre el UserDAO y la UI
public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private final MutableLiveData<User> loggedInUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        // Se recoge el contexto de la aplicacion para poder crear una instancia de la base de datos
        // y del repository de user
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        userRepository = new UserRepository(appDatabase.userDao());
        loggedInUser = new MutableLiveData<>();
    }

    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    // Ejecutamos las operaciones en la pool de hilos que habiamos creado en la base de datos
    // Asi las operaciones se ejecutaran de forma asincrona
    public void registerUser(User user, CallBackFuntions callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Long userId = userRepository.registerUser(user);
            if (userId != null) {
                loggedInUser.postValue(user);
                callback.onUserRegistered(true);
            } else {
                callback.onUserRegistered(false);
            }
        });
    }

    public void loginUser(String username, String password) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = userRepository.loginUser(username, password);
            loggedInUser.postValue(user);
        });
    }

    // Esta clase se encarga de crear instancias UserViewModel con el contexto de la aplicacion correcto
    // Sino no seria capaz de interactuar con el DAO o con la base de datos
    public static class UserViewModelFactory implements ViewModelProvider.Factory {
        private Application application;

        public UserViewModelFactory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UserViewModel(application);
        }
    }
}

