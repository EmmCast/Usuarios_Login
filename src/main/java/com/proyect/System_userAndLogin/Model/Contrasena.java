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


/**
 
  Entidad que encapsula la contraseña del usuario (en formato hash).
  Seguridad: El campo {@code contrasena} debe contener únicamente
  el hash (BCrypt/Argon2/PBKDF2). No almacenar contraseñas en claro.
 
  Relación 1:1 con {@link Usuario}. La columna {@code usuario_id} es única para forzar
  cardinalidad 1:1. Se recomienda la carga LAZY en el lado de {@link Usuario} para evitar
  exponer hashes en serializaciones accidentales.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10

 
 **/
@Entity
@Table(name = "contrasenas")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contrasena implements Serializable {

    private static final long serialVersionUID = 8564890825891991218L;

    /** Identificador único del registro de contraseña (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrasena")
    private Long idContrasena;

    /**
     Hash de la contraseña del usuario (obligatorio).
     Ej.: BCrypt con factor de costo apropiado.
     */
    @Column(name = "contrasena_hash", nullable = false)
    private String contrasena;

    /**
     Usuario propietario de esta contraseña (1:1).
     La unicidad en {@code usuario_id} asegura que un usuario tenga a lo sumo un registro.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
    
}
