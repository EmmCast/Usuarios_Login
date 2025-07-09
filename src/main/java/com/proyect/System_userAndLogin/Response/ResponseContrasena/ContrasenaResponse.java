package com.proyect.System_userAndLogin.Response.ResponseContrasena;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.ContrasenaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaResponse {
	
	public List<ContrasenaDto> contrasena;
	
	public List<ContrasenaDto> gerContrasena(){
		return contrasena;
	}
	
	public void setContrasena(List<ContrasenaDto> contrasena) {
		this.contrasena = contrasena;
	}

}
