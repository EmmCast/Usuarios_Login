package com.proyect.System_userAndLogin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyect.System_userAndLogin.Model.Usuario;

@Repository
public interface IUsuarioRepocitory extends JpaRepository<Usuario, Long>{

}
