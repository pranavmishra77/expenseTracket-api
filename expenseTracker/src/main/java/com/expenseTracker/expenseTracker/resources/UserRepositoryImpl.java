package com.expenseTracker.expenseTracker.resources;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{

	private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, FIRST_NAME,LAST_NAME,EMAIL,PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'),?,?,?,?)";
	private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL =?";
	private static final String SQL_FIND_BY_USERID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL,PASSWORD FROM ET_USERS WHERE USER_ID=?";
	private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM ET_USERS WHERE EMAIL=?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Integer create(String firstName, String lastname, String email, String password) throws etAuthException {
		
		String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection ->{PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, firstName);
			ps.setString(2, lastname);
			ps.setString(3, email);
			ps.setString(4, hashPassword);
			return ps;}, keyHolder);
			return (Integer)keyHolder.getKeys().get("USER_ID");
		}
		catch(Exception e) {
			throw new etAuthException("Invaild details faild to create account");
		}
		
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws etAuthException {
		try {
			User user =jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, email);
		if(!BCrypt.checkpw(password, user.getPassword()))
			throw new etAuthException("Unable to login Password does not Match");
		return user;
	} catch(EmptyResultDataAccessException e) {
		throw new etAuthException("Invalid Username(email)/Password");
	}
	}

	@Override
	public User findByUserId(Integer userId) throws etAuthException {
		return jdbcTemplate.queryForObject(SQL_FIND_BY_USERID, userRowMapper, userId);
	}

	@Override
	public Integer getcountByEmail(String email) throws etAuthException {
		return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
		
	}
	
	private RowMapper<User> userRowMapper = ((rs,rowNum) -> {
		return new User(rs.getInt("USER_ID"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),rs.getString("EMAIL"),rs.getString("PASSWORD"));
	});
	
	
}
