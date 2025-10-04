package com.proyect.System_userAndLogin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}