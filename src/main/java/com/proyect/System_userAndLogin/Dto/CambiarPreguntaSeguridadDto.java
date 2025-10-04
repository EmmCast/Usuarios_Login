package com.proyect.System_userAndLogin.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 
  DTO para cambiar la pregunta de seguridad.
  Requiere verificar la respuesta actual antes de establecer la nueva pregunta/respuesta.
  
  Flujo sugerido:
  1) Validar usuarioId.
  2) Verificar respuestaActual contra el hash almacenado.
  3) Hashear nuevaRespuesta y persistir con la nuevaPregunta.
  
  Validaciones:
  - usuarioId obligatorio y positivo.
  - respuestaActual, nuevaPregunta y nuevaRespuesta obligatorias.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPreguntaSeguridadDto {

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser positivo")
    private Long usuarioId;

    @NotBlank(message = "La respuesta actual es obligatoria")
    @Size(min = 3, max = 128, message = "La respuesta actual debe tener entre 3 y 128 caracteres")
    private String respuestaActual;

    @NotBlank(message = "La nueva pregunta es obligatoria")
    @Size(max = 255, message = "La nueva pregunta no puede exceder los 255 caracteres")
    private String nuevaPregunta;

    @NotBlank(message = "La nueva respuesta es obligatoria")
    @Size(min = 3, max = 128, message = "La nueva respuesta debe tener entre 3 y 128 caracteres")
    private String nuevaRespuesta;
}