package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
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
		if (a == null ) {
			throw new UnsupportedOperationException("Withdraw failed! The account could not be fond");
		} else if (!SessionCache.getCurrentUser().get().getAccounts().contains(a)) {
			throw new UnsupportedOperationException("Withdraw failed! The account selected to deposit to could not be found");
		} 
		double newBalance = a.getBalance() - amount;
		if (!a.isApproved()) {
			throw new UnsupportedOperationException("Withdraw failed! This account is not approved.");
		} else if (amount < 0) {
			throw new UnsupportedOperationException("Withdraw failed! The entered amount was negative.");
		} else if (newBalance < 0) {
			throw new OverdraftException("Withdraw failed! The resulting balance would result in an overdraft.");
		} 
		a.setBalance(newBalance);
		actDao.updateAccount(a);
		SessionCache.getCurrentUser().get().setAccounts(actDao.getAccountsByUser(SessionCache.getCurrentUser().get()));
	}
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		if (a == null ) {
			throw new UnsupportedOperationException("Deposit failed! The account could not be fond");
		} else if (!SessionCache.getCurrentUser().get().getAccounts().contains(a)) {
			throw new UnsupportedOperationException("Deposit failed! The account selected to deposit to could not be found");
		} 
		Double newBalance = a.getBalance() + amount;
		if (!a.isApproved()) {
			throw new UnsupportedOperationException("Deposit failed! This account is not approved.");
		} else if (amount < 0 ) {
			throw new UnsupportedOperationException("Deposit failed! The entered amount was negative.");
		}
		a.setBalance(newBalance);
		actDao.updateAccount(a);
		SessionCache.getCurrentUser().get().setAccounts(actDao.getAccountsByUser(SessionCache.getCurrentUser().get()));
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
		if (fromAct == null || toAct == null) {
			throw new UnsupportedOperationException("Transfer failed! At least one of the accounts could not be fond");
		} else if (!SessionCache.getCurrentUser().get().getAccounts().contains(fromAct)) {
			throw new UnsupportedOperationException("Transfer failed! The account selected to transfer to could not be found");
		} else if (!SessionCache.getCurrentUser().get().getAccounts().contains(toAct)) {
			throw new UnsupportedOperationException("Transfer failed! The account selected to transfer from could not be found");
		}
		double fromBal = fromAct.getBalance() - amount;
		double toBal = toAct.getBalance() + amount;
		if (!fromAct.isApproved() || !toAct.isApproved()) {
			throw new UnsupportedOperationException("Transfer failed! At least one of the accounts is unapproved");
		} else if (amount < 0 ) {
			throw new UnsupportedOperationException("Transfer failed! You cannot transfer a negative amount");
		} else if (fromBal < 0) {
			throw new UnsupportedOperationException("Transfer failed! The balance of the account withdrawn would have been negative");
		} else if (toBal < 0) {
			throw new UnsupportedOperationException("Transfer failed! The balance of the account to deposit would have been negative");
		} else if (fromAct.getId() == toAct.getId()) {
			throw new UnsupportedOperationException("Transfer failed! You cannot make a transfer to the same account");
		} 
				
		fromAct.setBalance(fromBal);
		toAct.setBalance(toBal);
		actDao.updateAccount(fromAct);
		actDao.updateAccount(toAct);
	}
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u, AccountType type) {
		Account a = new Account();
		a.setApproved(false);
		a.setBalance(STARTING_BALANCE);
		a.setOwnerId(u.getId());
		a.setType(type);
		actDao.addAccount(a);
		List<Account> accs = SessionCache.getCurrentUser().get().getAccounts();
		accs.add(a);
		SessionCache.getCurrentUser().get().setAccounts(accs);
		return a;
	}
	
	public Account createNewAccount(User u) {
		Account a = new Account();
		a.setApproved(false);
		a.setBalance(STARTING_BALANCE);
		a.setOwnerId(u.getId());
		actDao.addAccount(a);
		List<Account> accs = SessionCache.getCurrentUser().get().getAccounts();
		accs.add(a);
		SessionCache.getCurrentUser().get().setAccounts(accs);
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
				actDao.updateAccount(a);
				return a.isApproved();
			} else {
				throw new UnauthorizedException("Unauthorized attempt at approving or rejecting an account!");
			}
			
		}
		return false;
	}
}
