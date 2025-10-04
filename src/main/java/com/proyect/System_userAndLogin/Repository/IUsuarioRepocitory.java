package com.proyect.System_userAndLogin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Usuario;


/**
 
  Repositorio para la entidad {@link Usuario}.
  Proporciona operaciones CRUD y consultas personalizadas
  relacionadas con la gestión de usuarios.
  
  Contiene queries para buscar usuarios por nombre de usuario,
  email, estado (activo/inactivo) y autocompletado de nombres.
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/
@Repository
public interface IUsuarioRepocitory extends JpaRepository<Usuario, Long> {

    /**
     Verifica si existe un usuario con el nombre de usuario (ignora mayúsculas/minúsculas).
      
     @param nombreUsuario nombre de usuario a verificar
     @return true si existe, false si no
     
     */
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

    /**
     Obtiene una lista de nombres de usuario que inician con el prefijo dado.
     Se usa búsqueda insensible a mayúsculas/minúsculas.
      
     @param base prefijo base del nombre de usuario
     @return lista de coincidencias
     
     **/
    @Query(value = "select nombre_usuario from usuarios " +
            "where nombre_usuario ilike concat(:base, '%')", nativeQuery = true)
    List<String> findUsernames(@Param("base") String base);

    /**
     Busca un usuario activo por nombre de usuario.
     
     @param username nombre de usuario
     @return Optional con el usuario encontrado, vacío si no existe
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.nombre_usuario ILIKE :username AND u.estado = TRUE",
           nativeQuery = true)
    Optional<Usuario> findUsername(String username);

    /**
     Obtiene todos los usuarios activos.
      
     @return lista de usuarios con estado TRUE
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.estado = TRUE", nativeQuery = true)
    List<Usuario> findAllActivos();

    /**
     Busca un usuario activo por su id.
      
     @param idUsuario identificador único del usuario
     @return Optional con el usuario encontrado si está activo
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.id_usuario =:idUsuario AND u.estado = TRUE",
           nativeQuery = true)
    Optional<Usuario> findByIdActivo(Long idUsuario);

    /**
     Obtiene todos los usuarios inactivos.
      
     @return lista de usuarios con estado FALSE
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.estado = FALSE", nativeQuery = true)
    List<Usuario> findAllInactivos();

    /**
     Busca un usuario inactivo por nombre de usuario exacto.
      
     @param username nombre de usuario
     @return Optional con el usuario inactivo encontrado
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.nombre_usuario =:username AND u.estado = FALSE",
           nativeQuery = true)
    Optional<Usuario> findUsernameByInactivo(String username);

    /**
     Busca un usuario activo por su email.
      
     @param email dirección de correo electrónico
     @return Optional con el usuario activo encontrado
     
     **/
    @Query(value = "SELECT * FROM usuarios u WHERE u.email =:email AND u.estado = TRUE",
           nativeQuery = true)
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
