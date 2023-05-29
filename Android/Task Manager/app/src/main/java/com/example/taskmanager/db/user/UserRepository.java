package com.example.taskmanager.db.user;

class UserRepository {
    private UserDAO userDAO;

    public UserRepository(UserDAO userDAO) {
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

    public User loginUser(String username, String password) {
        return userDAO.loginUser(username, password);
    }
}