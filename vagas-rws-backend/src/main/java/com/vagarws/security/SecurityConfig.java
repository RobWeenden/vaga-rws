package com.vagarws.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vagarws.security.filter.JwtAuthFilter;
import com.vagarws.security.service.UsuarioDetailService;
import com.vagarws.security.util.JwtUtils;
import com.vagarws.util.ConstantsUtil;

/**
 * Class Responsavel pelas configurações de segurança de
 * authentication/authorization
 * 
 * @author robson.silva
 */
@Configuration
@EnableWebSecurity
@Profile(ConstantsUtil.PROFILE_DEV)
public class SecurityConfig {

	private JwtAuthFilter jwtAuthFilter;
	private UsuarioDetailService usuarioDetailService;
	private JwtUtils jwtUtils;
	private ObjectMapper objectMapper;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UsuarioDetailService usuarioDetailService, JwtUtils jwtUtils,
			ObjectMapper objectMapper) {
		super();
		this.jwtAuthFilter = jwtAuthFilter;
		this.usuarioDetailService = usuarioDetailService;
		this.jwtUtils = jwtUtils;
		this.objectMapper = objectMapper;
	}

	/**
	 * Metodo que realiza o authentication da aplicação
	 * 
	 * @author robson.silva
	 * @param http
	 * @return HttpSecurity
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		String[] pathsAllowed = { "/auth/**", "/swagger-ui/**", "v3/api-docs/**", "/empresa/registrar" };
		httpSecurity.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						(authorize) -> authorize.requestMatchers(pathsAllowed).permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

	/**
	 * Bean para criar/validar o passworda o UserDetails
	 * 
	 * @author robson.silva
	 * @return BCryptPasswordEncoder
	 */
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
