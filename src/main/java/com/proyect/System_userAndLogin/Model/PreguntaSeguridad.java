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
 
  Entidad que modela la pregunta de seguridad del usuario para recuperación de cuenta. 
  Seguridad: el campo {@code respuesta} almacena un hash (no texto plano).
  Se recomienda usar algoritmos de hashing seguros y con salt (p. ej., BCrypt, Argon2).
 
  Relación 1:1 con {@link Usuario}. La columna {@code usuario_id} es única para asegurar
  que cada usuario posea a lo sumo una pregunta de seguridad.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10

 
 **/

@Entity
@Table(name = "preguntas_seguridad")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridad implements Serializable {

    private static final long serialVersionUID = 8564890825891991218L;

    /** Identificador único de la pregunta (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    /** Texto de la pregunta de seguridad (obligatoria). */
    @Column(name = "pregunta", nullable = false)
    private String pregunta;

    /**
     Hash de la respuesta del usuario (obligatoria).
     Debe persistirse en forma de hash; nunca almacenar la respuesta en claro.
     */
    @Column(name = "respuesta_hash", nullable = false)
    private String respuesta;

    /**
     Usuario propietario de esta pregunta (1:1).
     La unicidad en {@code usuario_id} asegura cardinalidad 1:1.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}