package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;


/**
 
 Servicio para la gestión de preguntas de seguridad de usuarios.
  
 Se encarga de crear, actualizar y validar respuestas de seguridad
 asociadas a un usuario. Las respuestas deben ser hasheadas
 (BCrypt/Argon2) antes de persistir.
  
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

public interface IPreguntaSeguridadServices {

    /**
     Crea una pregunta de seguridad para un usuario.
     @param preguntaSeguridad DTO con pregunta y respuesta en claro (se hashea en el servicio)
     @return respuesta con la pregunta creada
     */
    ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(PreguntaSeguridadDto preguntaSeguridad);

    /**
     Obtiene la pregunta de seguridad por id de usuario.
     @param idUsuario id del usuario
     @return respuesta con la pregunta (y respuesta hasheada)
     */
    PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario);

    /**
     *Valida una respuesta de seguridad contra el hash almacenado.
     @param idUsuario id del usuario
     @param respuesta respuesta proporcionada en claro
     @return objeto base con metadatos que indican éxito o fallo
     */
    ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta);

    /**
     Cambia la pregunta de seguridad de un usuario.
     @param idUsuario id del usuario
     @param dto DTO con nueva pregunta y respuesta (en claro)
     @return respuesta con el resultado de la operación
     */
    ResponseEntity<PreguntaSeguridadResponseRest> cambiarPregunta(Long idUsuario, PreguntaSeguridadDto dto);

    /**
     Crea o actualiza la pregunta/respuesta de seguridad.
     @param idUsuario id del usuario
     @param pregunta nueva pregunta
     @param respuesta nueva respuesta en claro (se hashea)
     */
    void crearOActualizar(Long idUsuario, String pregunta, String respuesta);
}