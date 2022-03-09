package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

public class TransactionDaoDB implements TransactionDao {
	
	private static ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	public Transaction addTransaction(Transaction t) {
		Connection conn = connUtil.getConnection();
		String query = "INSERT INTO transactions (from_account_id, amount, type, timestamp) VALUES(?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, t.getSender().getId());
			pstmt.setDouble(2, t.getAmount());
			pstmt.setString(3, t.getType().name());
			pstmt.setObject(4, t.getTimestamp());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return t;
	}
	
	public Transaction addTransaction(Transaction t, Account to) {
		
		Connection conn = connUtil.getConnection();
		String query = "INSERT INTO transactions (from_account_id, to_account_id, amount, type, timestamp) VALUES(?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, t.getSender().getId());
			pstmt.setInt(2, to.getId());
			pstmt.setDouble(3, t.getAmount());
			pstmt.setString(4, t.getType().name());
			pstmt.setObject(5, t.getTimestamp());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}

	
	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		Connection conn = connUtil.getConnection();
		String query = "SELECT * FROM transactions";
		List<Transaction> transactions = new ArrayList<>();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				Transaction t = new Transaction();
				Account from = new Account();
				Account to = new Account();
				if(rs.getString("type").equals(TransactionType.WITHDRAWAL.name())) {
					t.setType(TransactionType.WITHDRAWAL);
					from.setId(rs.getInt("from_account_id"));
					t.setSender(from);
				} else if (rs.getString("type").equals(TransactionType.DEPOSIT.name())){
					t.setType(TransactionType.DEPOSIT);
					from.setId(rs.getInt("from_account_id"));
					t.setSender(from);
				} else {
					t.setType(TransactionType.TRANSFER);
					from.setId(rs.getInt("from_account_id"));
					to.setId(rs.getInt("to_account_id"));
					t.setSender(from);
					t.setRecipient(to);
				}
				t.setAmount(rs.getDouble("amount"));
				t.setTimestamp( (LocalDateTime) rs.getObject( "timestamp"));
				transactions.add(t);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return transactions;
	}

	

}
