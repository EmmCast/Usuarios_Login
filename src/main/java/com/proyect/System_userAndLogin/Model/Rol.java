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

/**
 
 	Entidad que representa los roles del sistema.	  
	Un rol puede estar asociado a múltiples usuarios
	y define permisos dentro de la aplicación.
	 
	@author Emmanuel 
 	@version 1.5
 	@since 2025-10

 **/
@Entity
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identificador único del rol (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    /** Nombre del rol (ejemplo: ADMIN, USER). */
    @Column(name = "rol")
    private String rol;

    /** Usuarios que poseen este rol (relación inversa N:M). */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<Usuario> usuarios = new HashSet<>();
}