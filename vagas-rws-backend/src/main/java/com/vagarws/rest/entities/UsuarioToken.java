package com.vagarws.rest.entities;

import java.time.LocalDateTime;

import com.vagarws.util.ConstantsUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = ConstantsUtil.USUARIO_TOKEN)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioToken {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ConstantsUtil.ID_USUARIO_TOKEN)
	private String id;

	@OneToOne
	@JoinColumn(name = ConstantsUtil.ID_USUARIO)
	private Usuario usuario;

	@Column(nullable = false, unique = true, columnDefinition = "TEXT")
	private String refreshToken;

	@Column(nullable = false)
	private LocalDateTime dataExpiracao;

}
