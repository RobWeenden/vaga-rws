package com.vagarws.rest.entities;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;

import com.vagarws.util.ConstantsUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = ConstantsUtil.ETAPA_CANDIDATURA)
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class EtapaCandidatura {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ConstantsUtil.ID_ETAPA_CANDIDATURA)
	private String id;
	private String descricao;
	private Boolean concluida;
	private LocalDate dataLimite;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = ConstantsUtil.ID_CANDIDATURA, nullable = true)
	private Candidatura candidatura;
	


}
