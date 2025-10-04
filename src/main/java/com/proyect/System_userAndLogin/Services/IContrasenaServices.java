package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.ActualizarContrasenaDto;
import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;


/**
 Servicio para gestión de contraseñas de usuarios.
  
 Incluye flujos de:
 - Creación de contraseña.
 - Cambio voluntario (usuario autenticado).
 - Primera vez: registrar pregunta de seguridad + nueva contraseña.
 - Recuperación por pregunta de seguridad.
 - Validación de contraseña (comparar contra hash).
  
 Las implementaciones deben aplicar hashing fuerte (BCrypt/Argon2),
 no registrar contraseñas en logs y limpiar referencias en memoria cuando sea posible.

 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

public interface IContrasenaServices {

    /**
     Crea una contraseña para el usuario indicado.
     @param creaContrasena DTO con idUsuario y contraseña en claro (se hashea)
     @return respuesta REST con metadatos del resultado
     */
    ResponseEntity<ContrasenaResponseRest> crearContrasena(ContrasenaDto creaContrasena);

    /**
     Cambia la contraseña validando la actual (flujo autenticado).
     @param idUsuario id del usuario
     @param actContra DTO con contraseña actual y nueva
     @return respuesta con el resultado del cambio
     */
    ContrasenaResponseRest actualizarContrasena(Long idUsuario, ActualizarContrasenaDto actContra);

    /**
     Flujo de primera vez: guarda pregunta de seguridad y establece contraseña nueva.
     @param idUsuario id del usuario
     @param dto DTO con pregunta y respuesta en claro
     @param nuevaContrasena contraseña nueva en claro (se hashea)
     @return respuesta con el resultado del proceso
     */
    ContrasenaResponseRest crearPreguntaYActualizarContrasena(Long idUsuario, PreguntaSeguridadDto dto, String nuevaContrasena);

    /**
     Recupera contraseña validando por email y respuesta de seguridad.
     @param email correo del usuario
     @param respuesta respuesta a la pregunta (en claro)
     @param nuevaContrasena contraseña nueva (en claro) a establecer
     @return respuesta con el resultado del proceso
     */
    ContrasenaResponseRest recuperarContrasena(String email, String respuesta, String nuevaContrasena);

    /**
     Valida una contraseña en claro contra el hash almacenado.
     @param idUsuario id del usuario
     @param contrasena contraseña en claro a validar
     @return respuesta con metadatos indicando si es válida
     */
    ContrasenaResponseRest validarContrasena(Long idUsuario, String contrasena);
}