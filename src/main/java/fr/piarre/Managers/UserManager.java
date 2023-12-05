package fr.piarre.Managers;

import fr.piarre.Exceptions.Auth.UserNotFoundException;
import fr.piarre.Exceptions.Auth.WrongPasswordException;
import fr.piarre.Models.LoginReturn;
import fr.piarre.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private Statement statement;

    public UserManager(Statement statement) {
        this.statement = statement;
    }

    public Boolean UserExists(Integer id) {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE id = '" + id + "'");

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return false;
        }
    }

    public User GetByEmail(String email) {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE email = '" + email + "'");

            if (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("actif"), resultSet.getInt("age"));
                user.setId(resultSet.getInt("id"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return null;
        }
    }

    public User GetUserById(Integer id) {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE id = '" + id + "'");

            if (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("actif"), resultSet.getInt("age"));
                user.setId(resultSet.getInt("id"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<User> GetUsersByEmail(String email) {
        List users = new ArrayList();

        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE email LIKE '%" + email + "%'");

            while (resultSet.next()) {
                User user = new User(resultSet.getString("email"), resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }

        return (ArrayList<User>) users;
    }

    public ArrayList<User> GetUsersByName(String name) {
        List users = new ArrayList();

        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE name LIKE '%" + name + "%'");

            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("actif"), resultSet.getInt("age"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }

        return (ArrayList<User>) users;
    }

    public ArrayList<User> GetUsersBySurName(String name) {
        List users = new ArrayList();

        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users WHERE surname LIKE '%" + name + "%'");

            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("actif"), resultSet.getInt("age"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }

        return (ArrayList<User>) users;
    }

    public LoginReturn Login(User user) {
        User foundedUser = GetByEmail(user.getEmail());

        if (foundedUser != null) {
            if (foundedUser.getPassword().equals(user.getPassword())) {
                return LoginReturn.SUCCESS;
            } else {
                return LoginReturn.WRONG_PASSWORD;
            }
        } else {
            return LoginReturn.USER_NOT_FOUND;
        }
    }

    public void AddUser(User user, User creator) {
        try {
            int res = this.statement.executeUpdate("INSERT INTO users(email, password, createdBy) VALUES ('" + user.getEmail() + "', '" + user.getPassword() + "', '" + creator.getEmail() + "')");
            User foundedUser = GetByEmail(user.getEmail());

            System.out.println("User added with id: " + foundedUser.getId());

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("User already exists with email : " + user.getEmail());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void DeleteUser(Integer id) {
        try {
            int res = this.statement.executeUpdate("DELETE FROM users WHERE id = " + id);
            System.out.println("User deleted with id: " + id);
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    public void UpdateUser(User user) {
        try {
            int res = this.statement.executeUpdate("UPDATE users SET name = '" + user.getName() + "', surname = '" + user.getSurname() + "', email = '" + user.getEmail() + "', password = '" + user.getPassword() + "', actif = '" + user.getActif() + "', age = '" + user.getAge() + "' WHERE id = " + user.getId());
            System.out.println("User updated with id: " + user.getId());
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void GetAllUsers() {
        try {
            ResultSet resultSet = this.statement.executeQuery("SELECT * FROM users");
            Integer count = 0;

            while (resultSet.next()) {
                System.out.println("User id: " + resultSet.getInt("id") + " | Email: " + resultSet.getString("email") + " | Created by: " + resultSet.getString("createdBy") + " | Created at: " + resultSet.getString("createdAt") + " | Updated at: " + resultSet.getString("updatedAt"));
                count++;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
}
