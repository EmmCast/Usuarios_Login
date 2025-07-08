package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rol implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Long idRol;
	
	@Column(name = "rol")
	private String rol;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Set<Usuario> usuarios = new HashSet<>();

}
