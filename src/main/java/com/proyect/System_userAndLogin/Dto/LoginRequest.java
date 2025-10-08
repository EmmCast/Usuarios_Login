package com.proyect.System_userAndLogin.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**

DTO ingresar el login.
La propiedad {@code contrasena} debe recibirse en texto plano y ser hasheada en la capa de servicio.

Validaciones:
- login no debe de estar vacio.
- password No debe de estar blanco.

Seguridad:
- Nunca loggear ni retornar la contraseña.
- Aplicar hashing seguro (BCrypt/Argon2) y limpiar la referencia en memoria cuando sea posible.

 
@author Emmanuel
@version 1.5
@since 2025-10

**/
@Data
public class LoginRequest {
	
	@NotBlank 
	String login;
	
    @NotBlank 
    String password
;
}
