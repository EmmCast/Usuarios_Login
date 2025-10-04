package com.proyect.System_userAndLogin.Response.ResponseContrasena;

import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
Respuesta REST extendida para contraseñas.
  
Incluye metadatos comunes (desde {@link ResponseRest})
y un objeto {@link ContrasenaResponse} con la data.
  
@author Emmanuel
@version 1.5
@since 2025-10

**/

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
