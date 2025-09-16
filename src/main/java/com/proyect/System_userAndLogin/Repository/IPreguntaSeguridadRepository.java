package com.proyect.System_userAndLogin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;

@Repository
public interface IPreguntaSeguridadRepository extends JpaRepository<PreguntaSeguridad, Long>{

	@Query(value = "SELECT EXISTS ( SELECT  FROM preguntasdseguridad ps WHERE ps.usuario_id = :idUsuario) ",nativeQuery = true)
	boolean existsByUsuarioId(Long idUsuario);

	@Query(value = "SELECT EXISTS ( SELECT  FROM preguntasdseguridad ps WHERE ps.usuario_id = :idUsuario) ",nativeQuery = true)
	boolean existsByUsuario_IdUsuario(Long usuarioId);

}
