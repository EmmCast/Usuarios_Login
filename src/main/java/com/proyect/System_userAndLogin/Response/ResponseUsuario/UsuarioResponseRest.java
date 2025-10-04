package com.proyect.System_userAndLogin.Response.ResponseUsuario;

import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 
  Respuesta REST extendida para usuarios.
  
  Incluye metadatos comunes (desde {@link ResponseRest})
  y un objeto {@link UsuarioResponse} con la lista de usuarios.
 

	@author Emmanuel
	@version 1.5
	@since 2025-10

**/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseRest extends ResponseRest{
	
	private UsuarioResponse usuarioResponse = new UsuarioResponse();
	
	public UsuarioResponse getUsuarioResponse() {
		return usuarioResponse;
	}

	public void setUsuarioResponse(UsuarioResponse usuarioResponse) {
		this.usuarioResponse = usuarioResponse;
	}
	
}
