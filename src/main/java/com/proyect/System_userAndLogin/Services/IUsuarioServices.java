package com.proyect.System_userAndLogin.Services;

import org.springframework.http.ResponseEntity;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;

public interface IUsuarioServices {
	
	ResponseEntity<UsuarioResponseRest> guardarUsuario(UsuarioDto dto);
	ResponseEntity<UsuarioResponseRest> buscarPorId(Long idUsuario);
	ResponseEntity<UsuarioResponseRest> buscarPorUserNameActivo(String username);
	ResponseEntity<UsuarioResponseRest> buscarTodosActivos();
	ResponseEntity<UsuarioResponseRest> buscarTodosInactivos();
	ResponseEntity<UsuarioResponseRest> eliminadoLogicoUsuario(Long idUsuario);
	ResponseEntity<UsuarioResponseRest> eliminadiFisicoUsuario(Long idUsuario);
	ResponseEntity<UsuarioResponseRest> eliminarUsuarioLogicoPorUserName(String userName);
	ResponseEntity<UsuarioResponseRest> eliminarUsuarioFisicoPorUserName(String userName);
	ResponseEntity<UsuarioResponseRest> actualizarUsuario(Long idUsuario, UsuarioDto dto);
	ResponseEntity<UsuarioResponseRest> reactivarUsuariosPorUsername(String username);
	
	 
}