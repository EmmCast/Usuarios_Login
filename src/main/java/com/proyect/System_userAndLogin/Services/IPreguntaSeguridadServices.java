package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;

public interface IPreguntaSeguridadServices {

	ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(PreguntaSeguridadDto preguntaSeguridad);
	
    PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario);
    
    ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta);
    
}
