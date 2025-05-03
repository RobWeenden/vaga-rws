package com.vagarws.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.vagarws.VagaRwsApplicationTests;
import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.PerfilAssembler;
import com.vagarws.rest.controller.PerfilController;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.repository.PerfilRepository;
import com.vagarws.rest.service.PerfilService;
import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.rest.to.response.PerfilResponse;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.FileUtils;

import jakarta.transaction.Transactional;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
public class PerfilTest extends VagaRwsApplicationTests {

//	<============== ConstantsUtil =========================>
	@SuppressWarnings("unused")
	private static final String URI_GET_PERFIL = UriComponentsBuilder.fromPath("/perfil/").toUriString();

//	<============== SERVICES MOCKS =========================>
	@InjectMocks
	private PerfilController perfilControllerMock;

	@InjectMocks
	private PerfilAssembler perfilAssemblerMock;

	@Mock
	private PerfilRepository perfilRepositoryMock;

	private PerfilService perfilServiceMock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		perfilServiceMock = new PerfilService(perfilRepositoryMock);
		perfilControllerMock = new PerfilController(perfilServiceMock, perfilAssemblerMock);

	}

	@SuppressWarnings("unchecked")
	@Test
	void testPerfilRegistrarSucess() {
		PerfilRequest perfilRequest = FileUtils.fetchScript("json/rest/perfil/perfil-registrar.json",
				PerfilRequest.class);

		PerfilResponse response = FileUtils.fetchScript("json/rest/perfil/perfil-response.json", PerfilResponse.class);
		EntityModel<PerfilResponse> entityModel = EntityModel.of(response);

		when(perfilRepositoryMock.save(any(Perfil.class))).thenAnswer(invocation -> {
			Perfil u = invocation.getArgument(0);
			u.setId("a7157107-d76e-4949-a977-d2cee2ca93Perfil");
			return u;
		});

		ResponseEntity<?> result = perfilControllerMock.create(perfilRequest);
		EntityModel<PerfilResponse> resultEntityModel = (EntityModel<PerfilResponse>) result.getBody();

		assertNotNull(result);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(entityModel.getContent(), resultEntityModel.getContent());

	}

	@SuppressWarnings("unchecked")
	@Test
	void testePerfilFindByIdSuccess() {
		String id = "a7157107-d76e-4949-a977-d2cee2ca93Perfil";
		Perfil perfil = new Perfil(id, "ROLE_TESTE");
		when(perfilRepositoryMock.findById(id)).thenReturn(Optional.of(perfil));

		PerfilResponse response = FileUtils.fetchScript("json/rest/perfil/perfil-response.json", PerfilResponse.class);
		EntityModel<PerfilResponse> entityModel = EntityModel.of(response);

		ResponseEntity<?> result = perfilControllerMock.findById(id);
		EntityModel<UsuarioResponse> resultEntityModel = (EntityModel<UsuarioResponse>) result.getBody();

		assertNotNull(result);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(entityModel.getContent(), resultEntityModel.getContent());
		assertNotNull(resultEntityModel.getLinks());
	}

	@Test
	void testePerfilFindByIdExceptionNotFound() {
		String id = "a7157107-d76e-4949-a977-d2cee2ca93";

		CustomException customException = assertThrows(CustomException.class, () -> {
			perfilControllerMock.findById(id);
		});

		assertNotNull(customException);
		assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
		assertEquals(ConstantsUtil.NOT_FOUND_MSG, customException.getMessage());
	}

}
