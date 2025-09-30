package com.proyect.System_userAndLogin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Contrasena;

@Repository
public interface IContrasenaReposiroty extends JpaRepository<Contrasena, Long>{

	@Query(value = "SELECT * FROM contrasenas c  WHERE c.usuario_id = :idUsuario",nativeQuery = true)
	Optional<Contrasena> findByUsuarioId(Long idUsuario);
	

}
