package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;

public interface IUsuarioServices {
	
	ResponseEntity<UsuarioResponseRest> guardarUsuario(UsuarioDto dto);
	ResponseEntity<UsuarioResponseRest> buscarPorId(Long idUsuario);
	ResponseEntity<UsuarioResponseRest> buscarTodos();
	ResponseEntity<UsuarioResponseRest> eliminarUsuario(Long idUsuario);
	ResponseEntity<UsuarioResponseRest> actualizarUsuario(Long idUsuario, UsuarioDto dto);
	 
}