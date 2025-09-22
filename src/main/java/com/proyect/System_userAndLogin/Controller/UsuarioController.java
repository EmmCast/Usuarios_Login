package com.proyect.System_userAndLogin.Controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;
import com.proyect.System_userAndLogin.Util.Util;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioServices usuarioServices;
	
	@PostMapping("/crear")
	public ResponseEntity<UsuarioResponseRest> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
	    return usuarioServices.guardarUsuario(usuarioDto);
	}

	/*
	@PostMapping("/saveUsuario")
	public ResponseEntity<UsuarioResponseRest> saveUsuario (
			@RequestParam("primerNombre")String primerNombre,
			@RequestParam("segundoNombre")String segundoNombre,
			@RequestParam("apellidoPaterno")String apellidoPaterno,
			@RequestParam("apellidoMaterno")String apellidoMaterno,
			@RequestParam("email")String email,
			@RequestParam("telefono")String telefono,
			@RequestParam("fotoUsuario")MultipartFile  fotoUsuario,
			@RequestParam("rolId")Set<Long>  rolId	
			)throws IOException{
	
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setPrimerNombre(primerNombre);
		usuarioDto.setSegundoNombre(segundoNombre);
		usuarioDto.setApellidoPaterno(apellidoPaterno);
		usuarioDto.setApellidoMaterno(apellidoMaterno);
		usuarioDto.setTelefono(telefono);
		usuarioDto.setEmail(email);
		usuarioDto.setRolesIds(rolId);
		usuarioDto.setFotoUsuario(Util.compressZLib(fotoUsuario.getBytes()));
		System.err.println(usuarioDto.toString());
		ResponseEntity<UsuarioResponseRest> response = usuarioServices.guardarUsuario(usuarioDto);
		
		return response;
	}
	*/

	@PostMapping("/saveUsuario")
	public ResponseEntity<UsuarioResponseRest> saveUsuario (
			@RequestParam ("primerNombre")String primerNombre,
			@RequestParam("segundoNombre")String segundoNombre,
			@RequestParam("apellidoPaterno")String apellidoPaterno,
			@RequestParam("apellidoMaterno")String apellidoMaterno,
			@RequestParam("email")String email,
			@RequestParam("telefono")String telefono,
			@RequestParam("fotoUsuario")MultipartFile  fotoUsuario,
			@RequestParam("rolId")Set<Long>  rolId	
			)throws IOException{
	
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setPrimerNombre(primerNombre);
		usuarioDto.setSegundoNombre(segundoNombre);
		usuarioDto.setApellidoPaterno(apellidoPaterno);
		usuarioDto.setApellidoMaterno(apellidoMaterno);
		usuarioDto.setTelefono(telefono);
		usuarioDto.setEmail(email);
		usuarioDto.setRolesIds(rolId);
		usuarioDto.setFotoUsuario(Util.compressZLib(fotoUsuario.getBytes()));
		
		System.err.println(usuarioDto.toString());
		ResponseEntity<UsuarioResponseRest> response = usuarioServices.guardarUsuario(usuarioDto);
		
		return response;
	}
	
	
}
