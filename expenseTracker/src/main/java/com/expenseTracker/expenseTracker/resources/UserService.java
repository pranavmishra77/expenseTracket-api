package com.expenseTracker.expenseTracker.resources;

public interface UserService {

	public User validateUser(String email, String password) throws etAuthException;
	public User registerUser(String FirstName, String Lastname, String email, String password) throws etAuthException;
}
