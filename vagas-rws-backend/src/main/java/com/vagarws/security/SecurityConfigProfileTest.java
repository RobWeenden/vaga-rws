package com.vagarws.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.vagarws.security.filter.JwtAuthFilter;
import com.vagarws.security.service.UsuarioDetailService;
import com.vagarws.security.to.response.JwtResponse;
import com.vagarws.security.util.JwtUtils;
import com.vagarws.util.ConstantsUtil;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Class Responsavel pelas configurações de segurança de
 * authentication/authorization
 * 
 * @author robson.silva
 */
@Configuration
@Profile(ConstantsUtil.PROFILE_TEST)
public class SecurityConfigProfileTest {

	private JwtAuthFilter jwtAuthFilter;
	private UsuarioDetailService usuarioDetailService;
	private JwtUtils jwtUtils;

	public SecurityConfigProfileTest(JwtAuthFilter jwtAuthFilter, UsuarioDetailService usuarioDetailService,
			JwtUtils jwtUtils) {
		super();
		this.jwtAuthFilter = jwtAuthFilter;
		this.usuarioDetailService = usuarioDetailService;
		this.jwtUtils = jwtUtils;
	}

	/**
	 * Metodo que realiza o authentication da aplicação
	 * 
	 * @author robson.silva
	 * @param httpSecurity
	 * @return HttpSecurity
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.disable())
				.headers(header -> header.frameOptions(Customizer.withDefaults()).disable()).logout(logout -> logout
						.logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
							response.addHeader(HttpHeaders.SET_COOKIE,
									this.jwtUtils.cleanCookieAccessToken().toString());
							response.addHeader(HttpHeaders.SET_COOKIE,
									this.jwtUtils.cleanCookieRefreshToken().toString());
							SecurityContextHolder.clearContext();
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().write(new JwtResponse(ConstantsUtil.USUARIO_DESLOGADO).getMessage());
						}));

		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Bean para autenticar UserDetails
	 * 
	 * @author robson.silva
	 * @param authConfig
	 * @return
	 * @throws Exception
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Método responsavel em definir qual serviço será usado para recuperar os dados
	 * do usuario durante o processo de autenticação
	 * 
	 * @author robson.silva
	 * @return
	 */
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(usuarioDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

}