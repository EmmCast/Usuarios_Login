package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 
 Contenedor para una lista de {@link PreguntaSeguridadDto}.
  
 Se utiliza dentro de {@link PreguntaSeguridadResponseRest} para
 devolver múltiples preguntas de seguridad asociadas a usuarios.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

//@JsonIgnoreProperties({ "preguntaSeguridads" })
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponse {
	
    /** Lista de preguntas de seguridad transferidas en la respuesta. */
//	@JsonProperty("preguntaSeguridad")
	private List<PreguntaSeguridadDto> preguntaSeguridad;
	
	public List<PreguntaSeguridadDto> getPreguntaSeguridad(){
		return preguntaSeguridad;
	}
	
	public void setPreguntaSeguridad(List<PreguntaSeguridadDto> preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

}
