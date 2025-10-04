package com.proyect.System_userAndLogin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.FotoUsuario;


/**
 Repositorio para la entidad {@link FotoUsuario}.
 Permite gestionar fotos de perfil asociadas a los usuarios.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 */

@Repository
public interface IFotoUsuarioRepository extends JpaRepository<FotoUsuario, Long> {

    /**
     Busca una foto de perfil por id de usuario.
      
     @param idUsuario id del usuario
     @return Optional con la foto encontrada
     
     */
    Optional<FotoUsuario> findByUsuario_IdUsuario(Long idUsuario);
}