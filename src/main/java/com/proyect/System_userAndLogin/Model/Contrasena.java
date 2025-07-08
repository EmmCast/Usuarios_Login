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
@Table(name = "Contrasenas")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contrasena  implements Serializable{
	
	private static final long serialVersionUID = 8564890825891991218L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_contrasena")
	private Long idcontrasena;
	
	@Column(name = "contrasena_hash")
	private String contrasena;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private Usuario usuario;	

}
