package com.proyect.System_userAndLogin.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 
  DTO para crear o actualizar la contraseña de un usuario.
  La propiedad {@code contrasena} debe recibirse en texto plano y ser hasheada en la capa de servicio.
  
  Validaciones:
  - idUsuario obligatorio y positivo.
  - contrasena con política mínima: 8–64, al menos 1 mayúscula, 1 minúscula, 1 dígito y 1 símbolo.
  
  Seguridad:
  - Nunca loggear ni retornar la contraseña.
  - Aplicar hashing seguro (BCrypt/Argon2) y limpiar la referencia en memoria cuando sea posible.
  
  Nota:
  - idContrasena es opcional; útil para operaciones de actualización.
    
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaDto {

    /** Puede venir nulo en creación. */
    private Long idContrasena;

    @NotNull(message = "El idUsuario es obligatorio")
    @Positive(message = "El idUsuario debe ser positivo")
    private Long idUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).{8,64}$",
        message = "La contraseña debe incluir al menos 1 mayúscula, 1 minúscula, 1 dígito y 1 símbolo"
    )
    private String contrasena;
}