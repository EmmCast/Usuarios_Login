package com.proyect.System_userAndLogin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Usuario;

@Repository
public interface IUsuarioRepocitory extends JpaRepository<Usuario, Long>{


    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

    @Query(value = "select nombre_usuario from usuarios " +
            "where nombre_usuario ilike concat(:base, '%')",
    nativeQuery = true)
    List<String> findUsernames(@Param("base") String base);
    
    @Query(value = "SELECT * FROM usuarios u WHERE u.nombre_usuario ILIKE :username AND u.estado = TRUE"
    		,nativeQuery = true)
	Optional<Usuario> findUsername(String username);

    @Query(value = "SELECT * FROM usuarios u WHERE u.estado = TRUE"
    		,nativeQuery = true)
	List<Usuario> findAllActivos();

    @Query(value = "SELECT * FROM usuarios u WHERE u.id_usuario =:idUsuario AND u.estado = TRUE "
    		,nativeQuery = true)
	Optional<Usuario> findByIdActivo(Long idUsuario);

    @Query(value = "SELECT * FROM usuarios u WHERE u.estado = False"
    		,nativeQuery = true)
	List<Usuario> findAllInactivos();

    @Query(value = "SELECT * FROM usuarios u WHERE u.nombre_usuario  =:username  AND u.estado = FALSE "
    		,nativeQuery = true)
	Optional<Usuario> findUsernameByInactivo(String username);

    @Query(value = "SELECT * FROM usuarios u WHERE u.email  =:email  AND u.estado = TRUE "
    		,nativeQuery = true) 
	Optional<Usuario> findByEmailIgnoreCase(String email);
	

}
