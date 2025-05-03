package com.vagarws;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.vagarws.security.filter.JwtAuthFilter;
import com.vagarws.security.util.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Class principal para configurações essenciais dos teste
 * 
 * @author robson.silva
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class VagaRwsApplicationTests {

// <=============== UTILS MOCKS =======================>
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext context;

	@MockBean	
	protected JwtUtils jwtUtils;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@MockBean
	protected JwtAuthFilter jwtAuthFilter;

	@Mock
	protected AuthenticationManager authenticationManager;

	@Mock
	protected Authentication authentication;

	@Autowired
	protected ApplicationContext applicationContext;

	@Mock
	protected HttpServletResponse response;

	/**
	 * Função para executar os teste unitarios, integração Função que verificar os
	 * endpoints que estão mapeados na aplicação
	 * 
	 * @author robson.silva
	 */
	@BeforeEach
	public void init() {
		this.mockMvc = webAppContextSetup(this.context).build();
		RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		handlerMapping.getHandlerMethods().forEach((key, value) -> {
			System.out.println("Path: " + key + " | Method: " + value);
		});
	}

}
