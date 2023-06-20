package com.expenseTracker.expenseTracker.resources;

public interface UserRepository {

	public Integer create(String firstName, String lastname, String email, String password) throws etAuthException;
	public User findByEmailAndPassword(String email, String password ) throws etAuthException;
	public User findByUserId(Integer userId) throws etAuthException;
	public Integer getcountByEmail(String email) throws etAuthException;
}
