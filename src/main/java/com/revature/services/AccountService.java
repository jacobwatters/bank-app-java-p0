package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.Transaction.TransactionType;
import com.revature.dao.AccountDao;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {
	
	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;
	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		
		double newBalance = a.getBalance() - amount;
		if (amount < 0 || !a.isApproved()) {
			throw new UnsupportedOperationException();
		}
		else if (newBalance < 0) {
			throw new OverdraftException();
		} 
		// Get the accounts transactions and add a new withdrawal type Transactions
		List<Transaction> transactions = a.getTransactions();
		Transaction t = new Transaction();
		t.setAmount(amount);
		t.setType(TransactionType.WITHDRAWAL);
		t.setTimestamp();
		transactions.add(t);
		// Set the new balance and transaction list of account a
		a.setBalance(newBalance);
		a.setTransactions(transactions);
		actDao.updateAccount(a);
	}
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		double newBalance = a.getBalance() + amount;
		if (amount < 0 || !a.isApproved()) {
			throw new UnsupportedOperationException();
		}
		List<Transaction> transactions = a.getTransactions();
		Transaction t = new Transaction();
		t.setAmount(amount);
		t.setType(TransactionType.DEPOSIT);
		t.setTimestamp();
		transactions.add(t);
		// Set the new balance and transaction list of account a
		a.setBalance(newBalance);
		a.setTransactions(transactions);
		actDao.updateAccount(a);
	}
	
	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) {
		double fromBal = fromAct.getBalance() - amount;
		double toBal = toAct.getBalance() + amount;
		if (amount < 0 || (fromBal < 0)) {
			throw new UnsupportedOperationException();
		}
		fromAct.setBalance(fromBal);
		toAct.setBalance(toBal);
		Transaction trans = new Transaction();
		Transaction trans2 = new Transaction();
		List<Transaction> transListFrom = fromAct.getTransactions();
		List<Transaction> transListTo = toAct.getTransactions();
		trans.setSender(fromAct);
		trans.setRecipient(toAct);
		trans.setAmount(amount);
		trans.setType(TransactionType.TRANSFER);
		trans2.setSender(fromAct);
		trans2.setRecipient(toAct);
		trans2.setAmount(amount);
		trans2.setType(TransactionType.TRANSFER);
		transListFrom.add(trans);
		transListTo.add(trans2);
		toAct.setTransactions(transListTo);
		actDao.updateAccount(fromAct);
		actDao.updateAccount(toAct);
	}
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		Account a = new Account();
		// Set the transactions of a newly created account to an empty ArrayList
		List<Transaction> transactions = new ArrayList<>();
		a.setTransactions(transactions);
		a.setApproved(false);
		a.setBalance(STARTING_BALANCE);
		a.setOwnerId(u.getId());
		actDao.addAccount(a);
		List<Account> accs;
		if (u.getAccounts() == null) {
			accs = new ArrayList<>();
		} else {
			accs = u.getAccounts();
		}
		accs = new ArrayList<>();
		accs.add(a);
		u.setAccounts(accs);
		return a;
	}
	
	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {
		Optional<User> u = SessionCache.getCurrentUser();
		if (u.isPresent()) {
			User user = u.get();
			if (user.getUserType() == User.UserType.EMPLOYEE) {
				a.setApproved(approval);
				return a.isApproved();
			} else {
				throw new UnauthorizedException();
			}
			
		}
		return false;
	}
}
