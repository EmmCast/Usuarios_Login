package com.proyect.System_userAndLogin.Dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import lombok.*;

/**
  
  DTO para registrar o actualizar la pregunta de seguridad del usuario.
  La respuesta se recomienda enviarla en texto plano aquí y hashearla en el servicio antes de persistir.
  
  Validaciones:
  - usuarioId obligatorio y positivo.
  - pregunta obligatoria.
  - respuesta obligatoria con longitud mínima.
  
  Seguridad:
  - No almacenar la respuesta en claro en BD; hashear con BCrypt/Argon2 en la capa de servicio.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadDto implements Serializable {

    private static final long serialVersionUID = 8564890825891991218L;

    @NotBlank(message = "La pregunta es obligatoria")
    @Size(max = 255, message = "La pregunta no puede exceder los 255 caracteres")
    private String pregunta;

    @NotBlank(message = "La respuesta es obligatoria")
    @Size(min = 3, max = 128, message = "La respuesta debe tener entre 3 y 128 caracteres")
    private String respuesta;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser un valor positivo")
    private Long usuarioId;
    
}