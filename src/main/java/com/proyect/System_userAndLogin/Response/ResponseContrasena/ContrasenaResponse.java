package com.proyect.System_userAndLogin.Response.ResponseContrasena;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.ContrasenaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 
 Contenedor para una lista de {@link ContrasenaDto}.
  
 Se utiliza dentro de {@link ContrasenaResponseRest} para devolver
 las contraseñas asociadas a un usuario (en formato hash, nunca en claro).
  
 Seguridad:
 - El campo contrasena en {@link ContrasenaDto} debe ser siempre un hash.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaResponse {
	
    /** Lista de contraseñas (hash) asociadas a usuarios. */
	public List<ContrasenaDto> contrasena;
	
	public List<ContrasenaDto> gerContrasena(){
		return contrasena;
	}
	
	public void setContrasena(List<ContrasenaDto> contrasena) {
		this.contrasena = contrasena;
	}

}
