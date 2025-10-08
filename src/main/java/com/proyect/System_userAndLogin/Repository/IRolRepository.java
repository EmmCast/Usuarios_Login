package com.proyect.System_userAndLogin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Rol;


/**
 Repositorio para la entidad {@link Rol}.
 Gestiona operaciones CRUD para roles del sistema.
  
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
	
	/**
	 Query que nos proporcionara todos los roles que tiene asignado un usuario 
	 @param idUsuario
	 @return
	 */
    @Query(value = "SELECT r.rol "
    		+ "FROM roles r "
    		+ "JOIN usuario_rol ur ON ur.rol_id = r.id_rol "
    		+ "JOIN usuarios u ON u.id_usuario = ur.usuario_id "
    		+ "WHERE u.id_usuario =:idUsuario AND u.estado = TRUE",
            nativeQuery = true)
	  List<String> findRoleNamesByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT exists(SELECT FROM roles r WHERE r.rol =:nombre)",
            nativeQuery = true)
	boolean existsByRol(String nombre);

    
    
}