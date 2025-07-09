package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;

public interface IContrasenaServices {

    ResponseEntity<ContrasenaResponseRest> crearContrasena(ContrasenaDto creaContrasena);
    
	 // Cambio voluntario (usuario autenticado)
    PreguntaSeguridadResponseRest actualizarContrasena(Long idUsuario, String contrasenaActual, String contrasenaNueva);
    
    // Primera vez: guarda la pregunta y la nueva contraseña
    PreguntaSeguridadResponseRest crearPreguntaYActualizarContrasena(Long idUsuario, PreguntaSeguridadDto dto, String nuevaContrasena);
    
    // Recuperación por pregunta
    PreguntaSeguridadResponseRest recuperarContrasena(String email, String respuesta, String nuevaContrasena);

    
    


    
}