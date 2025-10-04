package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;
import java.util.Date;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 
  Entidad que representa la foto de perfil del usuario.
  Almacena metadatos (nombre, fecha de creación) y los bytes crudos de la imagen
  en un campo {@code bytea}. Relación 1:1 con {@link Usuario}.
  Nota: Para servir imágenes, se recomienda exponer endpoints que lean
  los bytes desde BD con cabeceras adecuadas (Content-Type) o bien almacenar en objeto/blob storage.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10

 
 **/

@Entity
@Table(name = "fotos_usuarios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FotoUsuario implements Serializable {

    private static final long serialVersionUID = 8564890825891991218L;

    /** Identificador único de la foto (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto_usuario")
    private Long idFotoUsuario;

    /** Nombre/metadata de la imagen (por ejemplo, nombre original o MIME). */
    @Column(name = "nombre")
    private String nombre;

    /** Marca de tiempo de creación de la foto. */
    @Column(name = "creado_en")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creado = new Date();

    /**
     Contenido binario de la imagen.
     Se usa {@code bytea} (PostgreSQL). El tamaño y tipo pueden validarse en DB/servicio.
     */
    @Column(name = "foto", nullable = false, columnDefinition = "bytea")
    private byte[] foto;

    /**
     Usuario al que pertenece esta foto (1:1).
     La unicidad en {@code usuario_id} garantiza que cada usuario tenga a lo sumo una foto.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
