package com.proyect.System_userAndLogin.Services;

import com.proyect.System_userAndLogin.Model.FotoUsuario;


/**
 
 Servicio para la gestión de fotos de perfil de usuarios.
  
 Provee operaciones para guardar, actualizar, obtener y eliminar
 la imagen asociada a un usuario. Se recomienda validar tamaño,
 formato y tipo MIME en la implementación.

 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

public interface IFotoUsuarioServices {

    /**
     Guarda una nueva foto para el usuario indicado.
     @param idUsuario id del usuario
     @param imagen bytes de la imagen
     @return entidad {@link FotoUsuario} persistida
     */
    FotoUsuario guardarFoto(Long idUsuario, byte[] imagen);

    /**
     Actualiza la foto existente del usuario.
     @param idUsuario id del usuario
     @param imagen bytes de la nueva imagen
     @return entidad {@link FotoUsuario} actualizada
     */
    FotoUsuario actualizarFoto(Long idUsuario, byte[] imagen);

    /**
     Obtiene la foto por id de usuario.
     @param idUsuario id del usuario
     @return entidad {@link FotoUsuario} si existe, o null según la implementación
     */
    FotoUsuario obtenerFotoPorUsuarioId(Long idUsuario);

    /**
     Elimina la foto asociada al usuario.
     @param idUsuario id del usuario
     */
    void eliminarFoto(Long idUsuario);
}