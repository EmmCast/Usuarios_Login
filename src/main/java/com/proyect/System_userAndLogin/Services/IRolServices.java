package com.proyect.System_userAndLogin.Services;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.RolDto;
import com.proyect.System_userAndLogin.Model.Rol;

/**
 
 Servicio para la gestión del catálogo de roles.
  
 Permite listar roles disponibles y consultar un rol por su id.
 Idealmente, las operaciones de creación/edición de roles se
 restringen a perfiles administrativos.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

public interface IRolServices {

    /**
     Lista todos los roles existentes.
     @return lista de roles en formato DTO
     */
    List<RolDto> listarRoles();

    /**
     Busca un rol por su identificador.
     @param idRol id del rol
     @return entidad de rol si existe, o lanzar excepción controlada en implementación
     */
    Rol buscarPorId(Long idRol);
}