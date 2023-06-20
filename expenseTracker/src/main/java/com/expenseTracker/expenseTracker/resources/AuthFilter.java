package com.expenseTracker.expenseTracker.resources;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String authHeader = httpRequest.getHeader("Authorization");
		if(authHeader != null) {
			String[] authHeaderArr = authHeader.split("Bearer");
			if(authHeaderArr.length >1 && authHeaderArr[1] != null) {
				String token = authHeaderArr[1];
				try {
				Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
				httpRequest.setAttribute("userID", Integer.parseInt(claims.get("userId").toString()));
			} catch(Exception e) {
				httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"invalid/Expire token");
				return;
			}
			} else {
				httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Aothorization token must be Bearer");
				return;
			}	
			} else {
				httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Aothorization token must be provided");
				return;
			}
		doFilter(request, response, chain);
		chain.doFilter(request, response);
		
		
	}

}
