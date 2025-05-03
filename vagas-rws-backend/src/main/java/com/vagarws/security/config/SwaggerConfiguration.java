package com.vagarws.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vagarws.util.ConstantsUtil;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Class responsavel em configurar as informações da documentação da API 
 * para ser utilizar no swagger-ui
 * @author robson.silva
 */
@Configuration
public class SwaggerConfiguration {

	
	/**
	 * Metodo responsavel em customizar documentação da API
	 * 
	 * @author robson.silva
	 * @return
	 */
	@Bean
	public OpenAPI customOpenAPI() {
	    return new OpenAPI()
	    		//define a cabeçalho da documentação
	    		.info(new Info()
	    				.title("Vaga API")
						.description("Projeto destinado a fluxo de vagas interno")
						.version("1.0")
						.contact(new Contact()
									.name("Robson Silva")
									.email("robson.weenden@yahoo.com.br"))
						.license(new License()
									.name("License of API")
									.url("API license URL")))
	            //define a segurança do scheme para passar o token de validação
	            .components(new Components()
	                    .addSecuritySchemes(ConstantsUtil.SECURITY_SWAGGER,
	                            new SecurityScheme()
	                                    .type(SecurityScheme.Type.HTTP)
	                                    .scheme("bearer")
	                                    .bearerFormat("JWT"))	            )
	    //passar a parte de segurança de forma global
	    .security(List.of(new SecurityRequirement().addList(ConstantsUtil.SECURITY_SWAGGER)));
	}
	
}
