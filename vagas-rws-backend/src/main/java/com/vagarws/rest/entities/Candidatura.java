package com.vagarws.rest.entities;

import java.time.LocalDate;
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
import jakarta.persistence.ManyToOne;
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
@Table(name = ConstantsUtil.CANDIDATURA)
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class Candidatura {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ConstantsUtil.ID_CANDIDATURA)
	private String id;
	private String nome;
	private Boolean concluida;
	private LocalDate dataLimite;
	private LocalDate dataCandidatura;
	private String status;
	private String logo;
	private String empresa;
	private String cargo;

	@Column(columnDefinition = "TEXT")
	private String feedback;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = ConstantsUtil.ID_VAGA, nullable = true)
	private Vaga vaga;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = ConstantsUtil.ID_USUARIO_CANDIDATO, nullable = true)
	private Usuario usuarioCandidato;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = ConstantsUtil.ID_USUARIO_RESPONSAVEL, nullable = true)
	private Usuario usuarioResponsavel;

	@OneToMany(mappedBy = ConstantsUtil.CANDIDATURA, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<EtapaCandidatura> etapas;

}
