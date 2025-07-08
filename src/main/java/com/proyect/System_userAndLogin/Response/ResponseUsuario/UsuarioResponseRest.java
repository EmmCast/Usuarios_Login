package com.proyect.System_userAndLogin.Response.ResponseUsuario;

import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
