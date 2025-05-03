package com.vagarws.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vagarws.VagaRwsApplicationTests;
import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.UsuarioAssembler;
import com.vagarws.rest.controller.UsuarioController;
import com.vagarws.rest.entities.Credenciais;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.repository.PerfilRepository;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.rest.service.PerfilService;
import com.vagarws.rest.service.UsuarioService;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.rest.util.CustomMapperUtil;
import com.vagarws.security.repository.UsuarioTokenRepository;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.FileUtils;

import jakarta.transaction.Transactional;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
public class UsuarioTest extends VagaRwsApplicationTests {

//	<============== ConstantsUtil =========================>
	private static final String URI_GET_USUARIOS = UriComponentsBuilder.fromPath("/usuario/").toUriString();

//	<============== SERVICES MOCKS =========================>
	@InjectMocks
	private UsuarioController usuarioControllerMock;

	@InjectMocks
	private UsuarioAssembler usuarioAssemblerMock;

	@Mock
	private UsuarioRepository usuarioRepositoryMock;

	@Mock
	private PerfilRepository perfilRepositoryMock;

	@Mock
	private CustomMapperUtil customMapperUtilMock;

	@Mock
	private UsuarioTokenRepository usuarioTokenRepositoryMock;

	private PerfilService perfilServiceMock;
	private UsuarioService usuarioServiceMock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		perfilServiceMock = new PerfilService(perfilRepositoryMock);
		usuarioServiceMock = new UsuarioService(usuarioRepositoryMock, perfilServiceMock, customMapperUtilMock,
				usuarioTokenRepositoryMock);
		usuarioControllerMock = new UsuarioController(usuarioServiceMock, usuarioAssemblerMock);

	}

	@Test
	void testUsuarioRegistrarNotFoudWithValueNull() {
		UsuarioRequest usuarioRequest = null;
		CustomException customException = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.create(usuarioRequest);
		});
		assertNotNull(customException);
		assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
		assertEquals(ConstantsUtil.REQUISICAO_VAZIA, customException.getMessage());
	}

	@Test
	void testUsuarioRegistrarNotFoudWithValueEmpty() {
		UsuarioRequest usuarioRequest = new UsuarioRequest();
		CustomException customException = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.create(usuarioRequest);
		});

		assertNotNull(customException);
		assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
		assertEquals(ConstantsUtil.EMAIL_SENHA_OBRIGATORIOS, customException.getMessage());
	}

	@Test
	void testUsuarioRegistrarNotFoundPerfil() {
		UsuarioRequest usuarioRequest = new UsuarioRequest();
		usuarioRequest.setEmail("mock@email.com");
		usuarioRequest.setSenha("mockSenha");

		Perfil perfil = new Perfil();
		perfil.setId("a7157107-d76e-4949-a977-d2cee2ca93deRws");
		perfil.setPerfil("ROLE_MOCK");

		when(perfilRepositoryMock.findByPerfil("ROLE_MOCK")).thenReturn(Optional.of(perfil));
		CustomException customException = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.create(usuarioRequest);
		});

		assertNotNull(customException);
		assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
		assertEquals(ConstantsUtil.PERFIL_NOT_FOUND, customException.getMessage());
	}

	@Test
	void testUsuarioRegistrarExceptionDataIntegrityViolationException() {
		UsuarioRequest usuarioRequest = new UsuarioRequest();
		usuarioRequest.setEmail("admin@admin.com");
		usuarioRequest.setSenha("mockSenha");

		Perfil perfil = new Perfil();
		perfil.setId("a7157107-d76e-4949-a977-d2cee2ca93deRws");
		perfil.setPerfil("ROLE_USER");

		when(perfilRepositoryMock.findByPerfil("ROLE_USER")).thenReturn(Optional.of(perfil));
		when(usuarioRepositoryMock.save(any(Usuario.class)))
				.thenThrow(new DataIntegrityViolationException("Usuário já cadastrado"));

		CustomException customException = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.create(usuarioRequest);
		});

		assertNotNull(customException);
	}

	@Test
	void testUsuarioFindByIdExceptionNotFoud() {
		String id = "123";

		when(usuarioRepositoryMock.findById(id))
				.thenThrow(new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));

		CustomException customException = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.findById(id);
		});

		assertNotNull(customException);
		assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
		assertEquals(ConstantsUtil.NOT_FOUND_MSG, customException.getMessage());

	}

	@Test
	void testUsuarioFindByIdExceptionEmptyIdAndNullValue() {
		CustomException customExceptionEmpty = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.findById("");
		});

		assertNotNull(customExceptionEmpty);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionEmpty.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionEmpty.getMessage());

		CustomException customExceptionNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.findById(null);
		});

		assertNotNull(customExceptionNull);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionNull.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionNull.getMessage());

	}

	/**
	 * Teste de integração
	 * 
	 * @throws Exception
	 */
	@Test
	void testUsuarioFindByIdExceptionNotBlank() throws Exception {

		mockMvc.perform(post(URI_GET_USUARIOS + "").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.type").value("about:blank"))
				.andExpect(jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.detail").value("No static resource usuario."))
				.andExpect(jsonPath("$.instance").value(URI_GET_USUARIOS));

	}

	@SuppressWarnings("unchecked")
	@Test
	void testUsuarioGetAllSuccess() {
		List<UsuarioResponse> listaResponse = FileUtils.fetchScript("json/rest/usuario/usuario-response-lista.json",
				new TypeReference<List<UsuarioResponse>>() {
				});
		List<Usuario> usuarios = FileUtils.fetchScript("json/rest/usuario/usuario-lista.json",
				new TypeReference<List<Usuario>>() {
				});

		List<EntityModel<UsuarioResponse>> responseEntityModels = listaResponse.stream().map(usuarioResponse -> {
			EntityModel<UsuarioResponse> entityModel = EntityModel.of(usuarioResponse);
			entityModel.add(linkTo(methodOn(UsuarioController.class).findById(usuarioResponse.getId())).withSelfRel());

			return entityModel;
		}).toList();

		CollectionModel<EntityModel<UsuarioResponse>> usuarioResponseCollectionModel = CollectionModel
				.of(responseEntityModels);

		when(usuarioRepositoryMock.findAll()).thenReturn(usuarios);

		ResponseEntity<?> result = usuarioControllerMock.getAll();
		CollectionModel<EntityModel<UsuarioResponse>> resultEntityModel = (CollectionModel<EntityModel<UsuarioResponse>>) result
				.getBody();

		assertNotNull(result);
		assertEquals(HttpStatus.OK, result.getStatusCode());

		// compara o conteudo das coleções ignorando se são instancias diferentes
		assertNotNull(resultEntityModel.getLinks());
		assertEquals(usuarioResponseCollectionModel.getContent().size(), resultEntityModel.getContent().size());

	}

	@Test
	void testUsuarioDeleteByIdSuccess() {
		String id = "18993f4f-e2b3-44f5-b7d0-2d46a609963c344";
		Usuario usuarioRetorno = FileUtils.fetchScript("json/rest/usuario/usuario.json", Usuario.class);

		when(usuarioRepositoryMock.findById(id)).thenReturn(Optional.of(usuarioRetorno));
		when(usuarioRepositoryMock.save(any(Usuario.class))).thenAnswer(invocation -> {
			Usuario u = invocation.getArgument(0);
			u = usuarioRetorno;
			return u;
		});
		ResponseEntity<?> result = usuarioControllerMock.delete(id);
		assertNotNull(result);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	void testUsuarioDeleteByIdExceptionIdEmptyOrNull() {
		CustomException customExceptionIdEmpty = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.delete("");

		});

		assertNotNull(customExceptionIdEmpty);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionIdEmpty.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionIdEmpty.getMessage());

		CustomException customExceptionIdNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.delete(null);

		});

		assertNotNull(customExceptionIdNull);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionIdNull.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionIdNull.getMessage());
	}

	@Test
	void testUsuarioDeleteByIdNotFound() {
		String id = "123";
		when(usuarioRepositoryMock.findById(id))
				.thenThrow(new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
		CustomException customExceptionIdNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.delete(id);

		});

		assertNotNull(customExceptionIdNull);
		assertEquals(HttpStatus.NOT_FOUND, customExceptionIdNull.getStatus());
		assertEquals(ConstantsUtil.NOT_FOUND_MSG, customExceptionIdNull.getMessage());
	}

	@Test
	void testUsuarioUpdateSuccess() {
		String senha = "$2a$10$COX.yCeU9ZLLeKFPdnoKku2wF5e6bIMWxbvv06yvPpxXJEi5IQVwa";
		UsuarioRequest usuarioRequest = FileUtils.fetchScript("json/rest/usuario/usuario-update.json",
				UsuarioRequest.class);
		Usuario usuarioRetorno = FileUtils.fetchScript("json/rest/usuario/usuario.json", Usuario.class);
		usuarioRetorno.setNome(usuarioRequest.getNome());
		usuarioRetorno.setCredenciais(new Credenciais("TesteUpdate@teste.com", senha));

		when(usuarioRepositoryMock.findById(usuarioRequest.getId())).thenReturn(Optional.of(usuarioRetorno));
		when(usuarioRepositoryMock.save(any(Usuario.class))).thenAnswer(invocation -> {
			Usuario u = invocation.getArgument(0);
			u = usuarioRetorno;
			return u;
		});
		ResponseEntity<?> result = usuarioControllerMock.update(usuarioRequest);

		assertNotNull(result);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	void testUsuarioUpdateExceptionObjectNullAndIdNullOrEmpty() {
		UsuarioRequest usuarioRequest = null;

		CustomException customExceptionObjectNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.update(usuarioRequest);
		});

		assertNotNull(customExceptionObjectNull);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionObjectNull.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionObjectNull.getMessage());

		UsuarioRequest usuarioRequestIdNull = new UsuarioRequest();
		CustomException customExceptionObjectIdNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.update(usuarioRequestIdNull);
		});

		assertNotNull(customExceptionObjectIdNull);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionObjectNull.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionObjectNull.getMessage());

		UsuarioRequest usuarioRequestIdEmpty = new UsuarioRequest();
		usuarioRequestIdEmpty.setId("");
		CustomException customExceptionObjectIdEmpty = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.update(usuarioRequestIdEmpty);
		});

		assertNotNull(customExceptionObjectIdEmpty);
		assertEquals(HttpStatus.BAD_REQUEST, customExceptionObjectNull.getStatus());
		assertEquals(ConstantsUtil.DADO_INVALIDO, customExceptionObjectNull.getMessage());

	}

	@Test
	void testUsuarioUpdateExceptionNotFoud() {
		UsuarioRequest usuarioRequest = new UsuarioRequest();
		usuarioRequest.setId("123");

		when(usuarioRepositoryMock.findById("123"))
				.thenThrow(new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
		CustomException customExceptionObjectNull = assertThrows(CustomException.class, () -> {
			usuarioControllerMock.update(usuarioRequest);
		});

		assertNotNull(customExceptionObjectNull);
		assertEquals(HttpStatus.NOT_FOUND, customExceptionObjectNull.getStatus());
		assertEquals(ConstantsUtil.NOT_FOUND_MSG, customExceptionObjectNull.getMessage());

	}

}
