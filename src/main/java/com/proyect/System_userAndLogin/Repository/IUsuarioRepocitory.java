package com.proyect.System_userAndLogin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Usuario;

@Repository
public interface IUsuarioRepocitory extends JpaRepository<Usuario, Long>{


    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

    @org.springframework.data.jpa.repository.Query(value = "select nombre_usuario from usuarios " +
            "where nombre_usuario ilike concat(:base, '%')",
    nativeQuery = true)
    List<String> findUsernames(@Param("base") String base);
	

}
