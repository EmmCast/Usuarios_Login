package com.proyect.System_userAndLogin.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPreguntaSeguridadDto {
	
	private Long usuarioId;
	private String respuestaActual;
	private String nuevaPregunta;
	private String nuevaRespuesta;

}
