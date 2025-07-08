package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PreguntasDSeguridad")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridad  implements Serializable{
	
	private static final long serialVersionUID = 8564890825891991218L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pregunta  ")
	private Long idPregunta;
	
	@Column(name = "pregunta_hash")
	private String pregunta;
	
	@Column(name = "respuesta_hash")
	private String respuesta;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private Usuario usuario;

}
