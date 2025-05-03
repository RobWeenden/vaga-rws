package com.vagarws.security.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.vagarws.exceptions.CustomHttpServletException;
import com.vagarws.security.util.JwtUtils;
import com.vagarws.util.ConstantsUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Class responsavel em filtrar qualquer requisição
 * 
 * @author robson.silva
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";
	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;

	public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
		super();
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Metodo de filtro para autenticação e autorização
	 * 
	 * @author robson.silva
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(ConstantsUtil.AUTHORIZATION);
		String username = null;
		String jwt = null;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("access_token".equals(cookie.getName())) {
					jwt = cookie.getValue();
					username = jwtUtils.extrairUsername(jwt);
					break;
				} else if (authHeader != null && authHeader.startsWith(BEARER)) {
					jwt = authHeader.substring(7);
					username = jwtUtils.extrairUsername(jwt);
				}
			}
		}

		try {

			if (jwt != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtUtils.validarToken(jwt).equals(username)) {
					var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (TokenExpiredException e) {
			new CustomHttpServletException(response, request, ConstantsUtil.TOKEN_EXPIRADO, HttpStatus.UNAUTHORIZED);
		}

	}

}
