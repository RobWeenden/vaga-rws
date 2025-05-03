package com.vagarws;

import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.util.ConstantsUtil;

/**
 * Clazz de Configuração para rodar a carga inicial de usuarios padrão da
 * aplicação
 * 
 * @author robson.silva
 */
@Configuration
@Profile("!test")
public class RuntimeInitialLoad {

	private static final Logger logger = LoggerFactory.getLogger(RuntimeInitialLoad.class);

	@Autowired
	private UsuarioRepository userRepository;

	@Bean
	CommandLineRunner loadData(DataSource dataSource) {
		return args -> {
			logger.info("Iniciando a carga de dados {}", RuntimeInitialLoad.class);
			Optional<Usuario> optUser = userRepository.findByCredenciais_Email("admin@admin.com");

			if (!optUser.isPresent()) {
				logger.info("Executando script de inicialização 'datainitial.sql'...");
				ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
				populator.addScript(new ClassPathResource(ConstantsUtil.DATA_BASE_INIT));
				populator.execute(dataSource);
				logger.info("Script de inicialização executado com sucesso.");
			}
		};
	}

}
