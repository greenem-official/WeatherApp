package com.mireaweb.app.services;

import com.mireaweb.app.db.dao.UserDAO;
import com.mireaweb.app.db.model.User;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registering a user or logging in, with all the return variations, and a gew other tools
 */

public class UserService {
    private UserDAO userDAO = new UserDAO();
    public static int minPasswordLength = 6;

    public String register(String email, String password, String secondPassword) throws SQLException {
        if (email.equals("")) {
            return "empty email";
        } else if (checkValidEmail(email) == false) {
            return "incorrect email";
        }
        if (userDAO.ifUserExistsByEmail(email)) {
            return "registered already";
        } else if (password.trim().equals("") || secondPassword.trim().equals("")) {
            return "empty password";
        } else if (!password.equals(secondPassword)) {
            return "password mismatch";
        }
        else if (password.trim().length() < minPasswordLength || secondPassword.trim().length() < minPasswordLength) {
            return "short password";
        }
        else {
            int result = userDAO.addUser(new User(email, password));
            if (result == 0) {
                return "successful";
            } else if (result == -1) {
                return "error";
            }
        }
        return "undefined";
    }

    public String login(String email, String password) throws SQLException {
        User user = userDAO.findUser(email.trim(), password.trim());
        if (email.trim().equals("")) {
            return "empty email";
        } else if (checkValidEmail(email) == false) {
            return "incorrect email";
        }
        if (password.trim().equals("")) {
            return "empty password";
        }
        if (userDAO.ifUserExistsByEmail(email) == false) {
            return "no such user";
        }
//		else if(checkValidSymbols(password) == false){
//			return "wrong password"; //there can't be such symbols, let's not try what happens with them.
//		}
        else if ((user != null && user.getId() == 0)) {
            return "wrong password";
        } else if (user != null) {
            return "successful";
        } else {
            return "error";
        }
    }

    public User getUser(String email, String password) throws SQLException {
        User user = userDAO.findUser(email, password);
        return user;
    }
	
    public boolean checkValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean checkValidSymbols(String string) {
        String regex = "[^A-Za-z0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
