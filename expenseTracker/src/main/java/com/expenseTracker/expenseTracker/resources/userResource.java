package com.expenseTracker.expenseTracker.resources;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/users")
public class userResource {
	
	@Autowired
	UserService userService;
	
	
 @PostMapping("/register")
   //public ResponseEntity <Map<String,String>> registerUser(@RequestBody Map<String,Object> Usermap){
    public String registerUser(@RequestBody Map<String,Object> Usermap){
	String firstName = (String) Usermap.get("firstName");
	String lastName = (String) Usermap.get("lastName");
	String email = (String) Usermap.get("email");
	String password = (String) Usermap.get("password");
	User user = userService.registerUser(firstName, lastName, email, password);
	//return new ResponseEntity<>(generateJWTToken(user),HttpStatus.OK);
	return "Registered Successfully";
	}
 
 @PostMapping("/login")
     public String loginUser(@RequestBody Map<String,Object> Usermap){
 //public ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String,Object> Usermap){
	 String email = (String) Usermap.get("email");
	 String password = (String) Usermap.get("password");
	 User user = userService.validateUser(email, password);
	 //return new ResponseEntity<>(generateJWTToken(user),HttpStatus.OK);
	 return "User found Loging Successfull";
 }
 
 private Map<String, String> generateJWTToken(User user) {
	
	 long timestamp = System.currentTimeMillis();
	 String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY).setIssuedAt(new Date(timestamp)).setExpiration(new Date(timestamp+Constants.TOKEN_VALIDITY)).claim("userId", user.getUserId())
			 .claim("email", user.getEmail()).claim("firstName", user.getfirstName()).claim("lastName", user.getlastName()).compact();
	 System.out.println(token);
	 Map<String,String> map = new HashMap<>();
	 map.put("token", token);
	 return map;
 }
}
