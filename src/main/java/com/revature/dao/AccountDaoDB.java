package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {
	private static ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	
	public Account addAccount(Account a) {
		Connection conn = connUtil.getConnection();
		String query = "INSERT INTO accounts (user_id, balance, approved, type) VALUES(?, ?, ?, ?)";
		
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setBoolean(3, a.isApproved());
			pstmt.setString(4, a.getType().name());
			pstmt.executeUpdate();
	
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return a;
	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		String query = "SELECT * FROM accounts WHERE id=" + actId;
		Account account = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
	
			if (rs.next()) {
				account = new Account();
				account.setId(actId);
				account.setOwnerId(rs.getInt("user_id"));
				account.setBalance(rs.getDouble("balance"));
				account.setApproved(rs.getBoolean("approved"));
				//account.setType((AccountType) rs.getObject("type"));
				if (rs.getString("type") != null) {
					if(rs.getString("type").equals(AccountType.CHECKING.name())) {
						account.setType(AccountType.CHECKING);
					} else {
						account.setType(AccountType.SAVINGS);					}	
				}
				
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}

		return account;
	}

	public List<Account> getAccounts() {
		Connection conn = connUtil.getConnection();
		String query = "SELECT * FROM accounts";
		List<Account> accounts = new ArrayList<>();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				Account account = new Account();

				account.setId(rs.getInt("id"));
				account.setOwnerId(rs.getInt("user_id"));
				account.setBalance(rs.getDouble("balance"));
				account.setApproved(rs.getBoolean("approved"));
				if (rs.getString("type") != null) {
					if(rs.getString("type").equals(AccountType.CHECKING.name())) {
						account.setType(AccountType.CHECKING);
					} else {
						account.setType(AccountType.SAVINGS);
					}
				}
					
				accounts.add(account);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return accounts;
	}

	public List<Account> getAccountsByUser(User u) {
		Connection conn = connUtil.getConnection();
		String query = "SELECT * FROM accounts WHERE user_id=" +u.getId();
		List<Account> accounts = new ArrayList<>();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				Account account = new Account();

				account.setId(rs.getInt("id"));
				account.setOwnerId(rs.getInt("user_id"));
				account.setBalance(rs.getDouble("balance"));
				account.setApproved(rs.getBoolean("approved"));
				if(rs.getString("type").equals(AccountType.CHECKING.name())) {
					account.setType(AccountType.CHECKING);
				} else {
					account.setType(AccountType.SAVINGS);
				}
				accounts.add(account);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return accounts;
	}

	public Account updateAccount(Account a) {
		Connection conn = connUtil.getConnection();
		String query = "UPDATE accounts SET user_id=?, balance=?, approved=?, type=? where id=" + a.getId();
		//User user = null;
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setBoolean(3, a.isApproved());
			pstmt.setString(4, a.getType().name());
			int result = pstmt.executeUpdate();
			if (result == 0) {
				return null;
			}
		} catch (SQLException e) {
			// TODO: handle exception

			e.printStackTrace();

		}
		
		return a;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		String query = "DELETE FROM accounts WHERE id=" + a.getId();
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
	// for testing only
	public void truncateTable() {
		Connection conn = connUtil.getConnection();
		String query = "TRUNCATE accounts";


		try {

			stmt = conn.createStatement();
			//stmt.execute
			stmt.execute(query);

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

}
