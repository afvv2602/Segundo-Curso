package com.example.taskmanager.db.user;

class UserRepository {

    // El repositorio le permite acceder a la informacion al resto de la app
    // Se encarga de manejar la logica de cuando coger la informacion de internet
    // o cuando usar la base de datos local.
    private UserDAO userDAO;

    public UserRepository(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Long registerUser(User user) {
        User existingUser = userDAO.findUserByUsername(user.getUsername());
        if (existingUser == null) {
            return userDAO.insert(user);
        } else {
            return null;
        }
    }

    public User loginUser(String username, String password) { return userDAO.loginUser(username, password);}


}
