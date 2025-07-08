package com.proyect.System_userAndLogin.Services;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;

public interface IUsuarioServices {
	
	 UsuarioResponseRest guardarUsuario(UsuarioDto dto);
	 UsuarioResponseRest buscarPorId(Long idUsuario);
	 UsuarioResponseRest buscarTodos();
	 UsuarioResponseRest eliminarUsuario(Long idUsuario);
	 UsuarioResponseRest actualizarUsuario(Long idUsuario, UsuarioDto dto);
	 
}