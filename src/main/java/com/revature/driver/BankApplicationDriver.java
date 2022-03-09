package com.revature.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoDB;
import com.revature.dao.AccountDaoFile;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoDB;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.dao.UserDaoFile;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import com.revature.utils.SessionCache;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {
	
	private static final Logger logger = LogManager.getLogger(BankApplicationDriver.class);
	
	public static void printLine() {
		for (int i = 0; i < 78; i++) {
			System.out.print("*");
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		List<User> users = new ArrayList<>();
		UserDao udao = new UserDaoDB();
		AccountDao adao = new AccountDaoDB();
		TransactionDao tdao = new TransactionDaoDB();
		UserService userSrv = new UserService(udao, adao);
		AccountService accountSrv = new AccountService(adao);
		User currentUser = null;
		SessionCache.setCurrentUser(currentUser);
		BasicConfigurator.configure();
		boolean run = true;

		while (run) {
			// Handle login/register
			if (!SessionCache.getCurrentUser().isPresent()) {
					printLine();
					System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
					System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
					System.out.println("***** \t\t\t Welcome to Revature Bank \t\t\t *****");
					System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
					System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
					printLine();
					System.out.println("\n\t\t\t 1. Register");
					System.out.println("\t\t\t 2. Login");
					System.out.println("\t\t\t 3. Exit");
					System.out.print("Enter your Choice [1-3] :");
					choice = input.nextInt();

					switch (choice) {
						case 1:
							String username = null;
							String password = null;
							String firstName = null;
							String lastName = null;
							UserType userType = null;
							int option = 0;
							System.out.print("Enter first name : ");
							firstName = input.next();
							System.out.print("Enter last name : ");
							lastName = input.next();
							System.out.print("Enter username : ");
							username = input.next();
							System.out.print("Enter password : ");
							password = input.next();
							boolean userTypeEntered = false;
							while (!userTypeEntered) {
								System.out.println("Enter the user type");
								System.out.println("1. Customer");
								System.out.println("2. Employee");
								option = input.nextInt();
								if (option == 1 ) {
									userType = UserType.CUSTOMER;
									userTypeEntered = true;
								} else if (option == 2) {
									userType = UserType.EMPLOYEE;
									userTypeEntered = true;
								} else {
									logger.warn("Invalid input");
									System.out.println("Invalid input.");
								}
							}
							
							
							
							try {
								User u = new User();
								u.setUsername(username);
								u.setPassword(password);
								u.setFirstName(firstName);
								u.setLastName(lastName);
								u.setUserType(userType);
								userSrv.register(u);
								logger.info(username + " created an account");
								System.out.println("User successfully created!");
							} catch (UsernameAlreadyExistsException e) {
								logger.error("Username already exists");
								System.out.println("Username already exists! Can't create account.");
							}
							break;
						case 2:
							String uname = null;
							String pword = null;
							System.out.print("Enter username : ");
							uname = input.next();
							System.out.print("Enter password : ");
							pword = input.next();
							try {
								userSrv.login(uname, pword);
								System.out.println("Login successful!");
								logger.info(uname + " logged in");
							} catch (InvalidCredentialsException e) {
								System.out.println("Invalid username or password.");
							}
							break;
						case 3:
							logger.info("System exit");
							System.out.println("Thanks for using Revature Bank!!! Have a Nice Day!!!");
							input.close();
							System.exit(0);
							break;
						default:
							System.out.println("Invalid input!!");
							break;
					}
				
			} else if (SessionCache.getCurrentUser().get().getUserType() == UserType.CUSTOMER) {
				printLine();
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t Customer options \t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				printLine();
				System.out.println("\n\t\t\t 1. Apply for a banking account");
				System.out.println("\t\t\t 2. Manage your accounts");
				System.out.println("\t\t\t 3. Logout");
				System.out.print("Enter your Choice [1-3] :");
				choice = input.nextInt();
				switch (choice) {
				case 1:
					AccountType type = null;
					while(type == null) {
						printLine();
						System.out.println("\t\t\t 1. Checking");
						System.out.println("\t\t\t 2. Saving");
						System.out.print("Enter the type of account you're applying for [1-2] :");
						int nextChoice = input.nextInt();
						switch (nextChoice) {
							case 1:
								type = AccountType.CHECKING;
								break;
							case 2:
								type = AccountType.SAVINGS;
								break;
							default:
								logger.warn("Invalid input");
								System.out.println("Invalid input, try again!!");
								break;
						}
						
					}
					accountSrv.createNewAccount(SessionCache.getCurrentUser().get(), type);
					logger.info("Account created");
					System.out.println("New " + type.name() + " account created!");
					break;
				case 2:
					List<Account> accounts = adao.getAccountsByUser(SessionCache.getCurrentUser().get());
					printLine();
					System.out.println("Your Accounts  \t\t\t\t\t\t\t\t *****");
					printLine();
					System.out.println("ID \t Balance \t\tType \t\tApproved");
					accounts.forEach(a -> {
						System.out.println(a.getId() + " \t " + a.getBalance() + " \t\t\t" + a.getType() + "   \t" + a.isApproved());
					});
					printLine();
					System.out.println("\t\t\t 1. Make a deposit");
					System.out.println("\t\t\t 2. Make a withdrawal");
					System.out.println("\t\t\t 3. Make a transfer");
					System.out.print("Enter your Choice [1-3] :");
					int nextChoice = input.nextInt();
					
					int transId;
					double amt;
					Transaction t = new Transaction();
					try {
						switch (nextChoice) {
							case 1:
								System.out.println("Enter account id to deposit to");
								transId = input.nextInt();
								System.out.println("Enter amount to deposit");
								amt = input.nextDouble();
								Account d = adao.getAccount(transId);
								accountSrv.deposit(d, amt);
								t.setSender(d);
								t.setAmount(amt);
								t.setTimestamp();
								t.setType(TransactionType.DEPOSIT);
								tdao.addTransaction(t);
								logger.info("Deposit transaction");
								break;
							case 2:
								System.out.println("Enter account id to withdraw from");
								transId = input.nextInt();
								System.out.println("Enter amount to withdraw");
								amt = input.nextDouble();
								Account w = adao.getAccount(transId);
								accountSrv.withdraw(w, amt);
								t.setSender(w);
								t.setAmount(amt);
								t.setTimestamp();
								t.setType(TransactionType.WITHDRAWAL);
								tdao.addTransaction(t);
								logger.info("Withdrawal transaction");
								break;
							case 3:
								System.out.println("Enter account id to withdraw from");
								transId = input.nextInt();
								System.out.println("Enter account id to deposit to");
								int transIdTo = input.nextInt();
								System.out.println("Enter amount to transfer");
								amt = input.nextDouble();
								Account from = adao.getAccount(transId);
								Account to = adao.getAccount(transIdTo);
								accountSrv.transfer(from, to, amt);
								t.setSender(from);
								t.setAmount(amt);
								t.setTimestamp();
								t.setType(TransactionType.TRANSFER);
								tdao.addTransaction(t, to);
								logger.info("Transfer transaction");
								break;
							default:
								System.out.println("Invalid input!!");
								break;
						}
					} catch (UnsupportedOperationException | UnauthorizedException | OverdraftException e){
						logger.error(e.getMessage());
						System.out.println(e.getMessage());
					}
					break;
				case 3:
					logger.info("User logged out");
					System.out.println("Logout successful!");
					SessionCache.setCurrentUser(null);
					break;
				default:
					logger.warn("Invalid input");
					System.out.println("Invalid input!!");
					break;
				}
			} else {
				printLine();
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t Employee options \t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				System.out.println("***** \t\t\t\t\t\t\t\t\t *****");
				printLine();
				System.out.println("\n\t\t\t 1. Manage Customers");
				System.out.println("\t\t\t 2. View Transaction Log");
				System.out.println("\t\t\t 3. Logout");
				System.out.print("Enter your Choice [1-3] :");
				choice = input.nextInt();
				switch (choice) {
				case 1:
					List<Account> accounts = adao.getAccounts();
					printLine();
					System.out.println("User Accounts  \t\t\t\t\t\t\t\t *****");
					printLine();
					System.out.println("ID \t Balance \t\tType \t\tApproved");
					accounts.forEach(a -> {
						System.out.println(a.getId() + " \t " + a.getBalance() + " \t\t\t" + a.getType() + "   \t" + a.isApproved());
					});
					printLine();
					System.out.println("\n\t\t\t 1. Approve or reject an account");
					System.out.println("\t\t\t 2. Return");
					System.out.print("Enter your Choice [1-2] :");
					int nextChoice = input.nextInt();
					switch (nextChoice) {
						case 1:
							boolean approval = false;
							boolean wait = true;
							System.out.println("Enter the id of account to manange");
							int actId = input.nextInt();
							while (wait) {
								System.out.println("\t\t\t 1. Approve");
								System.out.println("\t\t\t 2. Reject");
								System.out.print("Set the approval of account with id " + actId + " by selecting [1-2] :");
								int appChoice = input.nextInt();
								switch (appChoice) {
									case 1:
										approval = true;
										wait = false;
										break;
									case 2:
										approval = false;
										wait = false;
										break;
									default:
										System.out.println("Invalid input!!");
										break;
								}
								accountSrv.approveOrRejectAccount(adao.getAccount(actId), approval);
							}
							break;
						case 2:
							break;
						default:
							System.out.println("Invalid input!!");
							break;
					}
					break;
				case 2:
					List<Transaction> transactions = tdao.getAllTransactions();
					printLine();
					System.out.println("Transactions \t\t\t\t\t\t\t\t *****");
					printLine();
					for(Transaction tLog: transactions) {
						TransactionType ty = tLog.getType();
						if (ty.equals(TransactionType.WITHDRAWAL)) {
							System.out.println("\t\t" + ty.name() + " of " + tLog.getAmount() + " from account " + tLog.getSender().getId() + " at " + tLog.getTimestamp());
						} else if (ty.equals(TransactionType.DEPOSIT)) {
							System.out.println("\t\t" + ty.name() + " of " + tLog.getAmount() + " into account " + tLog.getSender().getId() + " at " + tLog.getTimestamp());
						} else if (ty.equals(TransactionType.TRANSFER)){
							System.out.println("\t\t" + ty.name() + " of " + tLog.getAmount() + " from account " + tLog.getSender().getId() + " into account " + tLog.getRecipient().getId() +" at " + tLog.getTimestamp());
						}
					}
					break;
				case 3:
					logger.info("User logged out");
					System.out.println("Logout successful!");
					SessionCache.setCurrentUser(null);
					break;
				default:
					System.out.println("Invalid input!!");
					break;
				}
			}
		

		}
		
	}

}
