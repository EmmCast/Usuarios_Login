package com.proyect.System_userAndLogin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Contrasena;

/**
 Repositorio para la entidad {@link Contrasena}.
 Permite gestionar las contraseñas (hasheadas) de los usuarios.
 
 Seguridad:
 - Nunca exponer la contraseña en respuestas.
 - Siempre aplicar hashing fuerte (BCrypt/Argon2).
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 **/
@Repository
public interface IContrasenaReposiroty extends JpaRepository<Contrasena, Long> {

    /**
     Busca la contraseña asociada a un usuario por su id.
      
     @param idUsuario id del usuario
     @return Optional con la contraseña hasheada
     
     */
    @Query(value = "SELECT * FROM contrasenas c WHERE c.usuario_id = :idUsuario", nativeQuery = true)
    Optional<Contrasena> findByUsuarioId(Long idUsuario);
}