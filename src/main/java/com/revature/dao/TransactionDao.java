package com.revature.dao;

import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;

/**
 * The data access object interface for Transactions
 */
public interface TransactionDao {
	
	/**
	 * Adds a new transaction representing a withdrawal or deposit into the persistence layer
	 * @param t the Transaction object to add
	 * @param a the Account representing who to transfer to
	 * @return the same transaction that was added
	 */
	public Transaction addTransaction(Transaction t, Account a);
	/**
	 * Adds a new transaction representing a transfer into the persistence layer
	 * @param t the Transaction object to add
	 * @return the same transaction that was added
	 */
	public Transaction addTransaction(Transaction t);
	/**
	 * This method should retrieve all transactions of all accounts in chronological order
	 * @return a list of transactions ordered by timestamp
	 */
	public List<Transaction> getAllTransactions();
}
