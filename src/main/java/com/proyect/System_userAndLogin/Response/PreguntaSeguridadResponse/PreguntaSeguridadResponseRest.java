package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@JsonIgnoreProperties({ "pregunPreguntaSeguridad" })
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponseRest extends ResponseRest{
	
//    @JsonProperty("preguntaSeguridadResponse")
	private PreguntaSeguridadResponse preguntaSeguridadResponse = new PreguntaSeguridadResponse();

	public PreguntaSeguridadResponse getPreguntaSeguridadResponse() {
		return preguntaSeguridadResponse;
	}

	public void setUsuarioResponse(PreguntaSeguridadResponse preguntaSeguridadResponse) {
		this.preguntaSeguridadResponse = preguntaSeguridadResponse;
	}




}
