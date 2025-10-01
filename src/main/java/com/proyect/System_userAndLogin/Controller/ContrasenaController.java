package com.proyect.System_userAndLogin.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.ActualizarContrasenaDto;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;

@RestController
@RequestMapping("/v1/Contrasena")
public class ContrasenaController {

	private IContrasenaServices contrasenaServices;
	
	public ContrasenaController(IContrasenaServices contrasenaServices) {
		this.contrasenaServices = contrasenaServices;
	}
	
	@PutMapping("/actualizarContra/{idUsuario}")
	public ContrasenaResponseRest actualizaContra(@PathVariable("idUsuario") Long idUsuario, @RequestBody ActualizarContrasenaDto actContra){
		return contrasenaServices.actualizarContrasena(idUsuario, actContra);
	}
	
	@PostMapping("/validarContrasena/{idUsuario}")
	public ContrasenaResponseRest validarContrasena(@PathVariable("idUsuario") Long idUsuario, @RequestParam String contrasena) {
		return contrasenaServices.validarContrasena(idUsuario, contrasena);
	}
	
}
