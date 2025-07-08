package com.proyect.System_userAndLogin.Services;

import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;

public interface IPreguntaSeguridadServices {

    PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario);
    
    ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta);
    
}
