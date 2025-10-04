package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;

/**
 
 Servicio de negocio para la gestión de usuarios.
  
 Provee operaciones de creación, consulta, actualización y eliminación
 (lógica y física), además de búsquedas por identificadores y username,
 filtrando por estado (activo/inactivo) cuando aplica.
  
 Se recomienda que la implementación gestione:
 - Validación de DTOs y unicidades (email, username, teléfono).
 - Mapeo entre entidades y DTOs.
 - Manejo de errores con ResponseEntity y metadatos en la respuesta.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/
public interface IUsuarioServices {

    /**
     Crea un nuevo usuario a partir de un DTO.
     @param dto datos del usuario a crear
     @return respuesta con el usuario creado y metadatos
     */
    ResponseEntity<UsuarioResponseRest> guardarUsuario(UsuarioDto dto);

    /**
     Busca un usuario por su identificador.
     @param idUsuario id del usuario
     @return respuesta con el usuario encontrado (si existe)
     */
    ResponseEntity<UsuarioResponseRest> buscarPorId(Long idUsuario);

    /**
     Busca un usuario ACTIVO por su nombre de usuario.
     @param username nombre de usuario
     @return respuesta con el usuario activo si existe
     */
    ResponseEntity<UsuarioResponseRest> buscarPorUserNameActivo(String username);

    /**
     Lista todos los usuarios activos.
     @return respuesta con la lista de usuarios activos
     */
    ResponseEntity<UsuarioResponseRest> buscarTodosActivos();

    /**
     Lista todos los usuarios inactivos.
     @return respuesta con la lista de usuarios inactivos
     */
    ResponseEntity<UsuarioResponseRest> buscarTodosInactivos();

    /**
     Realiza la eliminación lógica del usuario (estado = FALSE).
     @param idUsuario id del usuario
     @return respuesta con el resultado de la operación
     */
    ResponseEntity<UsuarioResponseRest> eliminadoLogicoUsuario(Long idUsuario);

    /**
     Elimina físicamente al usuario (DELETE).
     @param idUsuario id del usuario
     @return respuesta con el resultado de la operación
     */
    ResponseEntity<UsuarioResponseRest> eliminadiFisicoUsuario(Long idUsuario);

    /**
     Elimina lógicamente por username (estado = FALSE).
     @param userName nombre de usuario
     @return respuesta con el resultado de la operación
     */
    ResponseEntity<UsuarioResponseRest> eliminarUsuarioLogicoPorUserName(String userName);

    /**
     Elimina físicamente por username (DELETE).
     @param userName nombre de usuario
     @return respuesta con el resultado de la operación
     */
    ResponseEntity<UsuarioResponseRest> eliminarUsuarioFisicoPorUserName(String userName);

    /**
     Actualiza datos del usuario.
     @param idUsuario id del usuario a actualizar
     @param dto datos nuevos del usuario
     @return respuesta con el usuario actualizado
     */
    ResponseEntity<UsuarioResponseRest> actualizarUsuario(Long idUsuario, UsuarioDto dto);

    /**
     Reactiva un usuario por username (estado = TRUE).
     @param username nombre de usuario
     @return respuesta con el usuario reactivado si procede
     */
    ResponseEntity<UsuarioResponseRest> reactivarUsuariosPorUsername(String username);
}
