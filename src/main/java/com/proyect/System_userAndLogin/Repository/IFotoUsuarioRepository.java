package com.proyect.System_userAndLogin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.FotoUsuario;

@Repository
public interface IFotoUsuarioRepository extends JpaRepository<FotoUsuario, Long>{

	Optional<FotoUsuario> findByUsuario_IdUsuario(Long idUsuario);
	
}
