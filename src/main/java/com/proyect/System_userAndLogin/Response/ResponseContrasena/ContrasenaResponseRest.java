package com.proyect.System_userAndLogin.Response.ResponseContrasena;

import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaResponseRest extends ResponseRest{
	
	private ContrasenaResponse contrasenaResponse = new ContrasenaResponse();

	public ContrasenaResponse getContrasenaResponse() {
		return contrasenaResponse;
	}
	
	public void setContrasenaResponse(ContrasenaResponse contrasenaResponse) {
		this.contrasenaResponse = contrasenaResponse;
	}
}
