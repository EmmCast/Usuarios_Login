package com.proyect.System_userAndLogin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;
import com.proyect.System_userAndLogin.Model.Usuario;


/**
 
 Repositorio para la entidad {@link PreguntaSeguridad}.
 Contiene consultas personalizadas para validar existencia
 y recuperar preguntas de seguridad asociadas a un usuario.
  
 Seguridad:
 - Solo devuelve la pregunta y la respuesta hasheada.
 - Nunca debe exponer la respuesta en texto plano.
    
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 
 **/

@Repository
public interface IPreguntaSeguridadRepository extends JpaRepository<PreguntaSeguridad, Long> {

    /**
     Verifica si un usuario ya tiene registrada una pregunta de seguridad.
      
     @param idUsuario id del usuario
     @return true si existe, false si no
     */
    @Query(value = "SELECT EXISTS (SELECT FROM preguntas_seguridad ps WHERE ps.usuario_id = :idUsuario)", nativeQuery = true)
    boolean existsByUsuarioId(Long idUsuario);

    /**
     Variante de verificación de pregunta de seguridad para un usuario.
      
     @param usuarioId id del usuario
     @return true si existe, false si no
     */
    @Query(value = "SELECT EXISTS (SELECT FROM preguntas_seguridad ps WHERE ps.usuario_id = :usuarioId)", nativeQuery = true)
    boolean existsByUsuario_IdUsuario(Long usuarioId);

    /**
     Obtiene la pregunta de seguridad asociada al usuario.
      
     @param idUsuario id del usuario
     @return Optional con la pregunta de seguridad
     
     */
    @Query(value = "SELECT * FROM preguntas_seguridad ps WHERE ps.usuario_id = :idUsuario", nativeQuery = true)
    Optional<PreguntaSeguridad> findByIdUser(Long idUsuario);

    /**
     Alias de findByIdUser (misma lógica).
      
     @param idUsuario id del usuario
     @return Optional con la pregunta de seguridad
     
     */
    @Query(value = "SELECT * FROM preguntas_seguridad ps WHERE ps.usuario_id = :idUsuario", nativeQuery = true)
    Optional<PreguntaSeguridad> findByIdUsuario(Long idUsuario);
}
