package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@JsonIgnoreProperties({ "preguntaSeguridads" })
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponse {
	
//	@JsonProperty("preguntaSeguridad")
	private List<PreguntaSeguridadDto> preguntaSeguridad;
	
	public List<PreguntaSeguridadDto> getPreguntaSeguridad(){
		return preguntaSeguridad;
	}
	
	public void setPreguntaSeguridad(List<PreguntaSeguridadDto> preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

}
