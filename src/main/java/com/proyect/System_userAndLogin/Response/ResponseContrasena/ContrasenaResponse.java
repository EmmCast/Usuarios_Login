package com.proyect.System_userAndLogin.Response.ResponseContrasena;

import java.util.List;

import com.proyect.System_userAndLogin.Model.Contrasena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaResponse {
	
	public List<Contrasena> contrasena;
	
	public List<Contrasena> gerContrasena(){
		return contrasena;
	}
	
	public void setContrasena(List<Contrasena> contrasena) {
		this.contrasena = contrasena;
	}

}
