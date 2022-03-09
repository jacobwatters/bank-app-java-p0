package com.revature.services;

import java.util.List;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on users
 */
public class UserService {
	
	UserDao userDao;
	AccountDao accountDao;
	
	public UserService(UserDao udao, AccountDao adao) {
		this.userDao = udao;
		this.accountDao = adao;
	}
	
	/**
	 * Validates the username and password, and return the User object for that user
	 * @throws InvalidCredentialsException if either username is not found or password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) {
		User login = userDao.getUser(username, password);
		if (login == null) {
			throw new InvalidCredentialsException();
		} else {
			login.setAccounts(accountDao.getAccountsByUser(login));
			SessionCache.setCurrentUser(login);
			return login;
		}
	}
	
	/**
	 * Creates the specified User in the persistence layer
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {
		List<User> users = userDao.getAllUsers();
		users.forEach(user -> {
			if (user.getUsername().equals(newUser.getUsername())) {
				throw new UsernameAlreadyExistsException();
			}
		});
		userDao.addUser(newUser);
	}
}
