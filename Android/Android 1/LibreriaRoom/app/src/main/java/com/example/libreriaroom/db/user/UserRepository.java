package com.example.libreriaroom.db.user;

class UserRepository {

    // El repositorio le permite acceder a la informacion al resto de la app
    // Se encarga de manejar la logica de cuando coger la informacion de internet
    // o cuando usar la base de datos local.
    private UserDAO userDAO;

    public UserRepository(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Long registerUser(User user) {
        return userDAO.registerUser(user);
    }

    public User loginUser(String username, String password) {
        return userDAO.loginUser(username, password);
    }
}
