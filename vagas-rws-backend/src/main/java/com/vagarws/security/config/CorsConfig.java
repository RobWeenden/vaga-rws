package com.vagarws.security.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class Responsavel em liberar os CORS que são bloqueando quando ha uma
 * comunicação com o client
 * 
 * @author robson.silva
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
	private static final long MAXAGESECS = 3600;

	@Value("${client.cors.url}")
	private String clientCorsUrl;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(clientCorsUrl)
				.allowedMethods(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name())
				.allowedHeaders("*").allowCredentials(true).maxAge(MAXAGESECS);
	}
}
