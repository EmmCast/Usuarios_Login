package com.proyect.System_userAndLogin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;

@Repository
public interface IPreguntaSeguridadRepository extends JpaRepository<PreguntaSeguridad, Long>{

}
