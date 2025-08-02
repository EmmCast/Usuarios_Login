package com.proyect.System_userAndLogin.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IRolServices;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;

@RestController
@RequestMapping("/Usuario")
public class UsuarioController {
	
	@Autowired
	private IUsuarioServices usuarioServices;
	
	@PostMapping("/crear")
	public ResponseEntity<UsuarioResponseRest> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
	    return usuarioServices.guardarUsuario(usuarioDto);
	}

}
