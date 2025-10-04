package com.proyect.System_userAndLogin.Response.ResponseUsuario;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**

	Contenedor para una lista de {@link UsuarioDto}.
  
	Se utiliza dentro de {@link UsuarioResponseRest} para devolver
	usuarios (activos/inactivos, según el caso).
 
	@author Emmanuel
	@version 1.5
	@since 2025-10

**/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    /** Lista de usuarios transferidos en la respuesta. */
	private List<UsuarioDto> usuario;
	
	public List<UsuarioDto> getUsuario(){
		return usuario;
	}
	
	public void setUsuario(List<UsuarioDto> usuario) {
		this.usuario = usuario;
	}
	
}
