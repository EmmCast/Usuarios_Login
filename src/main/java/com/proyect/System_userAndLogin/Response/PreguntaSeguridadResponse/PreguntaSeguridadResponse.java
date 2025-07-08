package com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse;

import java.util.List;

import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadResponse {
	
	private List<PreguntaSeguridad> preguntaSeguridad;
	
	public List<PreguntaSeguridad> getPreguntaSeguridads(){
		return preguntaSeguridad;
	}
	
	public void setPreguntaSeguridad(List<PreguntaSeguridad> preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

}
