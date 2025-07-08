package com.proyect.System_userAndLogin.Response.ResponseUsuario;

import java.util.List;

import com.proyect.System_userAndLogin.Model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

	private List<Usuario> usuario;
	
	public List<Usuario> getUsuario(){
		return usuario;
	}
	
	public void setUsuario(List<Usuario> usuario) {
		this.usuario = usuario;
	}
	
}
