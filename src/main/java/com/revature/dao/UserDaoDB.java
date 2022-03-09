package com.revature.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao {
	
	private static ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	
	public User addUser(User user) {
		Connection conn = connUtil.getConnection();
		String query = "INSERT INTO users (first_name, last_name, username, password, user_type) VALUES(?, ?, ?, ?, ?)";
		
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getUsername());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getUserType().name());
			pstmt.executeUpdate();
	
		} catch (SQLException e) {

			e.printStackTrace();

		}

		return user;
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		Statement stmt;
		ResultSet rs;
		String query = "SELECT * FROM users WHERE id = " + userId;
		User user = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
	
			if (rs.next()) {
				user = new User();
				user.setId(userId);
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				if(rs.getString("user_type").equals(UserType.CUSTOMER.name())) {
					user.setUserType(UserType.CUSTOMER);
				} else {
					user.setUserType(UserType.EMPLOYEE);
				}	
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}

		return user;
	}

	public User getUser(String username, String password) {
		Connection conn = connUtil.getConnection();
		Statement stmt;
		ResultSet rs;
		String query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
		User user = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				//user.setUserType((UserType) rs.getObject("user_type"));
				if(rs.getString("user_type").equals(UserType.CUSTOMER.name())) {
					user.setUserType(UserType.CUSTOMER);
				} else {
					user.setUserType(UserType.EMPLOYEE);
				}		
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}

		return user;
	}

	public List<User> getAllUsers() {
		Connection conn = connUtil.getConnection();
		String query = "SELECT * FROM users";

		List<User> userList = new ArrayList<>();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				User user = new User();

				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				//user.setUserType((UserType) rs.getObject("user_type"));
				if(rs.getString("user_type").equals(UserType.CUSTOMER.name())) {
					user.setUserType(UserType.CUSTOMER);
				} else {
					user.setUserType(UserType.EMPLOYEE);
				}	
				userList.add(user);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return userList;
	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		String query = "UPDATE users SET username=?, password=?, firstName=?, lastName=?, userType=? where id=" + u.getId();
		//User user = null;
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());
			pstmt.setString(3, u.getFirstName());
			pstmt.setString(4, u.getUsername());
			pstmt.setString(5, u.getUserType().name());
			int result = pstmt.executeUpdate();
			if (result == 0) {
				return null;
			}
		} catch (SQLException e) {
			// TODO: handle exception

			e.printStackTrace();

		}
		
		return u;
	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		String query = "DELETE FROM users WHERE id=" + u.getId();

		boolean status = false;

		try {

			stmt = conn.createStatement();
			if (stmt.executeUpdate(query) != 0) {
				status = true;
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return status;
	}
	public void truncateTable() {
		Connection conn = connUtil.getConnection();
		String query = "TRUNCATE users";


		try {

			stmt = conn.createStatement();
			//stmt.execute
			stmt.execute(query);

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

}
