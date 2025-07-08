package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponseRest extends ResponseRest{
	
	private PreguntaSeguridadResponse preguntaSeguridadResponse = new PreguntaSeguridadResponse();

	public PreguntaSeguridadResponse getpregunPreguntaSeguridad() {
		return preguntaSeguridadResponse;
	}

	public void setUsuarioResponse(PreguntaSeguridadResponse preguntaSeguridadResponse) {
		this.preguntaSeguridadResponse = preguntaSeguridadResponse;
	}
}
