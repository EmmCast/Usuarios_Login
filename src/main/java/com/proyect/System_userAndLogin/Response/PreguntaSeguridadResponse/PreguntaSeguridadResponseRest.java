package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;


import com.proyect.System_userAndLogin.Response.ResponseRest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 
 Respuesta REST extendida para preguntas de seguridad.
  
 Incluye metadatos comunes (desde {@link ResponseRest})
 y un objeto {@link PreguntaSeguridadResponse} con la data solicitada.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

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
