package com.revature.dao;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.User;

/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {
	// use this file location to persist the data to
	public static String fileLocation = "src\\accounts.txt";

	public Account addAccount(Account a) {
		// TODO Auto-generated method stub
		//System.out.println();
		List<Account> accs = getAccounts();
		accs.add(a);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			oos.writeObject(accs);
		} catch (IOException e) {
			System.out.println("IOException in addAccount output");
		}
		return a;
	}
	

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub
		Account a = null;
		List<Account> accs = getAccounts();
		for (Account account: accs) {
			if (account.getId().equals(actId)) {
				a = account;
				break;
			}
		}
		addAccountsToStream(accs);
		return a;
	}

			
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		List<Account> accs = new ArrayList<>();;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileLocation))) {
			// Check if the object is available to be read;
			accs = (List<Account>) ois.readObject();
			
		} catch (FileNotFoundException e) {
			return accs;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception " + e.getClass().getName() + " thrown in getAccounts()");
		}
		return accs;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub
		List<Account> userAccs = new ArrayList<>();
		List<Account> accs = getAccounts();
		for (Account a : accs) {
			if (a.getOwnerId().equals(u.getId())) {
				userAccs.add(a);
			}
		}
		addAccountsToStream(accs);
		return userAccs;
	}

	public Account updateAccount(Account a) {
		// TODO Auto-generated method stub
		List<Account> accs = getAccounts();
		try {
			if (accs.removeIf(acc -> acc.getId().equals(a.getId()))) {
				accs.add(a);
				return a;
			}
			
		} finally {
			addAccountsToStream(accs);
		}
		return null;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub
		List<Account> accs = getAccounts();
		boolean success = accs.removeIf(acc -> acc.equals(a));
		addAccountsToStream(accs);
		return success;
	}
	
	public void addAccountsToStream(List<Account> accs) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			oos.writeObject(accs);
		} catch (IOException e) {
			System.out.println("IOException in addAccount output");
		}
	}

}
