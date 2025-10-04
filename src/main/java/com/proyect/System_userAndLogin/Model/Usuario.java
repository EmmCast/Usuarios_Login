package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
  
  Entidad que representa a un usuario dentro del sistema.
  Incluye datos de identificación, contacto, estado, marca de tiempo de creación/actualización
  y relaciones con {@link Rol}, {@link FotoUsuario}, {@link Contrasena} y {@link PreguntaSeguridad}.
  Restricciones:
    nombre_usuarioy email son únicos.
    telefono es único.
    La tupla (primer_nombre, segundo_nombre, apellido_paterno, apellido_materno) es única.
  Notas: Para información sensible (contraseña, pregunta de seguridad) se usa
  carga perezosa (LAZY) con el fin de evitar cargas y serializaciones accidentales.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10

 
 **/

@Entity
@Table(name = "Usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "primer_nombre", "segundo_nombre", "apellido_paterno", "apellido_materno" })
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

    private static final long serialVersionUID = -3774119125088387327L;

    /** Identificador único del usuario (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    /** Alias utilizado para autenticación. Debe ser único. */
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    /** Primer nombre del usuario (obligatorio). */
    @Column(name = "primer_nombre", nullable = false)
    private String primerNombre;

    /** Segundo nombre del usuario. (Tu esquema lo marca no nulo). */
    @Column(name = "segundo_nombre", nullable = false)
    private String segundoNombre;

    /** Apellido paterno (obligatorio). */
    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    /** Apellido materno (obligatorio). */
    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    /** Correo electrónico único y obligatorio. */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /** Teléfono único y obligatorio. */
    @Column(name = "telefono", nullable = false, unique = true)
    private String telefono;

    /** Fecha/hora de alta del usuario. Se inicializa al crear la instancia. */
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso = new Date();

    /** Última fecha/hora de actualización del registro. */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at = new Date();

    /** Estado del usuario: TRUE = activo, FALSE = inactivo. */
    @Column(name = "estado", nullable = false)
    private Boolean estado;

    /**
     Conjunto de roles asignados al usuario (relación N:M).
     Se usa carga EAGER para tener roles disponibles en autenticación/autorización.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    /**
     Foto de perfil asociada (relación 1:1 bidireccional).
     Cascade ALL para que persista/borre junto con el usuario. Carga EAGER para mostrar foto rápidamente.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private FotoUsuario fotoUsuario;

    /**
     * Contraseña asociada (hash) del usuario (relación 1:1 bidireccional).
     * Carga LAZY para evitar exponerla/serializarla por defecto.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contrasena contrasena;

    /**
     * Pregunta de seguridad del usuario (relación 1:1 bidireccional).
     * Carga LAZY para minimizar exposición de datos sensibles.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PreguntaSeguridad preguntaSeguridad;
}