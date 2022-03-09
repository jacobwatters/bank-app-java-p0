package com.revature.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;

/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {
	
	public static String fileLocation = "src\\users.txt";

	public User addUser(User user) {
		// TODO Auto-generated method stub
		List<User> users = getAllUsers();
		users.add(user);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			oos.writeObject(users); 
		} catch (IOException e) {
			System.out.println("IOException thrown");
		} finally {
			addUsersToStream(users);
		}
		return user;
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		User u = null;
		List<User> users = getAllUsers();
		for (User user: users) {
			if (user.getId().equals(userId)) {
				u = user;
				break;
			}
		}
		addUsersToStream(users);
		return u;
	}

	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub
		User u = null;
		List<User> users = getAllUsers();
		for (User user: users) {
			if ((user.getUsername().equalsIgnoreCase(username)) && (user.getPassword().equalsIgnoreCase(pass))) {
				u = user;
				break;
			}
		}
		addUsersToStream(users);
		return u;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users = new ArrayList<>();
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileLocation))) {
			users = (List<User>) ois.readObject();
		} catch (FileNotFoundException e) {
			return users;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception thrown in getAllUsers()");
		}
		return users;
	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub
		List<User> users = getAllUsers();
		
		try {
			if (users.removeIf(user -> user.getId().equals(u.getId()))) {
				users.add(u);
				return u;
			}
			
		} finally {
			addUsersToStream(users);
		}
		return null;
	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub
		List<User> users = getAllUsers();
		boolean greatSuccess = users.removeIf(user -> user.equals(u));
		addUsersToStream(users);
		return greatSuccess;
	}
	
	private void addUsersToStream(List<User> users) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			oos.writeObject(users);
		} catch (IOException e) {
			System.out.println("IOException thrown in addUsersToStream");
		}
	}

}
