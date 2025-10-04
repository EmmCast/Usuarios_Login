package com.proyect.System_userAndLogin.Dto;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
  DTO para transferencia de datos de usuarios entre capas (Controller ⇄ Service).
  Incluye datos personales, contacto, metadatos y los IDs de roles asociados.
  No expone entidades ni colecciones con objetos pesados para mantener el desacoplamiento.
  
  Validaciones destacadas:
  - Email con formato válido y longitud máxima.
  - Teléfono de exactamente 10 dígitos.
  - rolesIds no puede estar vacío al crear/actualizar.
  - Fechas no pueden estar en el futuro.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {

    private static final long serialVersionUID = -7970922455652136120L;

    /** Identificador del usuario (puede ser nulo en operaciones de creación). */
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String primerNombre;

    @Size(max = 50, message = "El segundo nombre no puede exceder los 50 caracteres")
    private String segundoNombre;

    @NotBlank(message = "El Apellido Paterno es obligatorio")
    @Size(max = 30, message = "El Apellido Paterno no puede exceder los 30 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El Apellido Materno es obligatorio")
    @Size(max = 30, message = "El Apellido Materno no puede exceder los 30 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 50, message = "El email no puede exceder los 50 caracteres")
    private String email;

    @NotBlank(message = "El Teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El Teléfono debe contener exactamente 10 dígitos")
    private String telefono;

    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]*$", message = "El nombre de usuario solo puede contener letras, números, punto, guion y guion bajo")
    private String nombreUsuario;

    /** Foto de perfil en bytes. Opcional. */
    private byte[] fotoUsuario;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private Date fechaIngreso;

    @NotEmpty(message = "Debe indicar al menos un rol")
    private Set<@NotNull(message = "El id del rol no puede ser nulo")
               @Positive(message = "El id del rol debe ser positivo") Long> rolesIds = new HashSet<>();
}