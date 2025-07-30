package com.proyect.System_userAndLogin.Response.ResponseUsuario;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

	private List<UsuarioDto> usuario;
	
	public List<UsuarioDto> getUsuario(){
		return usuario;
	}
	
	public void setUsuario(List<UsuarioDto> usuario) {
		this.usuario = usuario;
	}
	
}
