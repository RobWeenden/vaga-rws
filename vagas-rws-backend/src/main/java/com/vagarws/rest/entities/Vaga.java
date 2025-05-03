package com.vagarws.rest.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = ConstantsUtil.VAGA)
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class Vaga {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ConstantsUtil.ID_VAGA)
	private String id;

	private String titulo;
	private String empresa;
	private String localidade;
	private String regime;
	private String modalidade;
	private String faixaSalarial;

	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;

	private List<String> requisitos;
	private List<String> beneficios;
	private List<String> diferenciais;

	private String sobreEmpresa;
	private String logoEmpresa;
	private String emailContato;
	private String status;
	private Long qtdCandidaturas;
	private LocalDate dataPublicacao;
	private LocalDate dataPrazoInscricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = ConstantsUtil.ID_USUARIO, nullable = true)
	private Usuario usuarioResponsavel;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = ConstantsUtil.USUARIO_CANDIDATO, joinColumns = @JoinColumn(name = ConstantsUtil.ID_VAGA), inverseJoinColumns = @JoinColumn(name = ConstantsUtil.ID_USUARIO))
	private List<Usuario> listaUsuarioCandidatos = new ArrayList<>();

	private Boolean isCandidato;

	@OneToMany(mappedBy = ConstantsUtil.VAGA, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Candidatura> listaCandidatura = new ArrayList<>();

	public void adicionarCandidato(Usuario usuario) {
		this.listaUsuarioCandidatos.add(usuario);
		usuario.getListaVagasCandidatos().add(this);
	}

	public void removerCandidato(Usuario usuario) {
		this.listaUsuarioCandidatos.remove(usuario);
		usuario.getListaVagasCandidatos().remove(this);
	}

	public Long adicionarQtdCandidatura(long valor) {
		return this.qtdCandidaturas = this.qtdCandidaturas + valor;
	}

	public Long removerQtdCandidatura(long valor) {
		return this.qtdCandidaturas = this.qtdCandidaturas - valor;
	}

}
