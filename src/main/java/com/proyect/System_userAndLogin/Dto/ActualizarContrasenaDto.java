package com.proyect.System_userAndLogin.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  DTO para actualizar la contraseña del usuario verificando la contraseña actual.
  
  Validaciones:
  - contrasenaActual y contrasenaNueva obligatorias.
  - contrasenaNueva cumple política mínima: 8–64, con mayúscula, minúscula, dígito y símbolo.
  
  Reglas recomendadas:
  - contrasenaNueva debe ser diferente de contrasenaActual (validación en servicio).
  - Invalidar sesiones activas tras el cambio.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10

 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarContrasenaDto {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contrasenaActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La nueva contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).{8,64}$",
        message = "La nueva contraseña debe incluir al menos 1 mayúscula, 1 minúscula, 1 dígito y 1 símbolo"
    )
    private String contrasenaNueva;
}