package com.proyect.System_userAndLogin.Dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 
  DTO para la transferencia de Roles.
  Mantiene solo los datos necesarios (id y nombre del rol).
  
  Validaciones:
  - rol obligatorio y con longitud razonable (3–30).
  
  Nota: El catálogo de roles suele ser administrado por un módulo distinto.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador del rol. Puede ser nulo al crear uno nuevo. */
    private Long idRol;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 30, message = "El rol debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "^[A-Z_]+$", message = "El rol debe estar en MAYÚSCULAS y puede incluir guiones bajos (ej. ADMIN, SUPER_ADMIN)")
    private String rol;
    
}