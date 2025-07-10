package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponse {
	
	private List<PreguntaSeguridadDto> preguntaSeguridad;
	
	public List<PreguntaSeguridadDto> getPreguntaSeguridads(){
		return preguntaSeguridad;
	}
	
	public void setPreguntaSeguridad(List<PreguntaSeguridadDto> preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

}
