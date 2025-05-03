package com.vagarws.rest.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.util.ConstantsUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class entity do usuario
 * 
 * @author robson.silva
 */
@Entity
@Table(name = ConstantsUtil.USUARIO)
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ConstantsUtil.ID_USUARIO)
	private String id;
	private Boolean ativo;
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = ConstantsUtil.USUARIO_PERFIL, joinColumns = @JoinColumn(name = ConstantsUtil.ID_USUARIO), inverseJoinColumns = @JoinColumn(name = ConstantsUtil.ID_PERFIL))
	private Set<Perfil> listaPerfil;

	private Credenciais credenciais;

	private String cargo;
	private String telefone;
	private String departamento;

	private LocalDate dataCadastro;

	@OneToMany(mappedBy = ConstantsUtil.USUARIO_RESPONSAVEL, fetch = FetchType.LAZY)
	private List<Vaga> vagas;

	@ManyToMany(mappedBy = "listaUsuarioCandidatos", fetch = FetchType.LAZY)
	private List<Vaga> listaVagasCandidatos = new ArrayList<>();

	@OneToMany(mappedBy = "usuarioCandidato", fetch = FetchType.LAZY)
	private List<Candidatura> listaCandidatura;

	public Usuario(UsuarioRequest usuarioRequest) {
		this.id = usuarioRequest.getId();
		this.ativo = true;
		this.cpf = usuarioRequest.getCpf();
		this.dataNascimento = usuarioRequest.getDataNascimento();
		this.nome = usuarioRequest.getNome();
		this.credenciais = new Credenciais(usuarioRequest.getEmail(), usuarioRequest.getSenha());

	}

	public Usuario(UsuarioRequest usuarioRequest, Perfil perfil) {
		this.id = usuarioRequest.getId();
		this.ativo = true;
		this.cpf = usuarioRequest.getCpf();
		this.dataNascimento = usuarioRequest.getDataNascimento();
		this.nome = usuarioRequest.getNome();
		this.credenciais = new Credenciais(usuarioRequest.getEmail(), usuarioRequest.getSenha());
		this.telefone = usuarioRequest.getTelefone();
		this.dataCadastro = usuarioRequest.getDataCadastro();
		this.departamento = usuarioRequest.getDepartamento();
		this.cargo = usuarioRequest.getCargo();
		this.listaPerfil = new HashSet<>(Arrays.asList(perfil));

	}

	public Usuario(String email, String senha, String nome, Perfil perfil) {
		this.ativo = true;
		this.credenciais = new Credenciais(email, senha);
		this.listaPerfil = new HashSet<>(Arrays.asList(perfil));
		this.nome = nome;

	}

	public Usuario(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
	}

}
