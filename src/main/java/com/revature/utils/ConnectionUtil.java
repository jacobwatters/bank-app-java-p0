package com.revature.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	private static ConnectionUtil cu = null;
	private static Properties prop;
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	/**
	 * This method should read in the "database.properties" file and load
	 * the values into the Properties variable
	 */
	private ConnectionUtil() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")){
			prop = new Properties();
			prop.load(input);
			// load a properties file
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("usr");
			password = prop.getProperty("pswd");
		} catch(IOException e) {
			e.printStackTrace();
		}   
		
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(cu == null)
			cu = new ConnectionUtil();
		return cu;
	}
	
	/**
	 * This method should create and return a Connection object
	 * @return a Connection to the database
	 */
	public Connection getConnection() {
		// Hint: use the Properties variable to setup your Connection object
		Connection connection = null;
		try {
			// dburl = jdbc:mysql://localhost:3306/revature
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
		return connection;
	}
}
