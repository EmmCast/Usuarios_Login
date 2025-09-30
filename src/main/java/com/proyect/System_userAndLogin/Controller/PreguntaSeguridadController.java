package com.proyect.System_userAndLogin.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.CambiarPreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Services.IPreguntaSeguridadServices;

@RestController
@RequestMapping("/v1/preguntaSeguridad")
public class PreguntaSeguridadController {
	
	private IPreguntaSeguridadServices preguntaServ;
	
	public PreguntaSeguridadController(IPreguntaSeguridadServices preguntaServ) {
		this.preguntaServ = preguntaServ;
	}
	
	@PostMapping("/preguntaSeguridad")
	public ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(@RequestBody PreguntaSeguridadDto pregunta){

		return preguntaServ.crearPregunta(pregunta);
	}
	
	@PostMapping("/verificarPregunta/{idUsuario}")
	public ResponseRest verificarPregunta(@PathVariable("idUsuario")Long idUsuario, @RequestParam("Respuesta") String Respuesta){
		return preguntaServ.validarRespuestaSeguridad(idUsuario, Respuesta);
	}
	
	
	@GetMapping("/obtenerPreguntaporIdUsuario/{idUsuario}")
	public PreguntaSeguridadResponseRest obtenerpreguntaPorIdUsuario(@PathVariable("idUsuario") Long idUsuario){
		return preguntaServ.obtenerPorUsuarioId(idUsuario);
	}
	
	@PutMapping("/cambiarPregunta/{idUsuario}")
	public ResponseEntity<PreguntaSeguridadResponseRest> actualizar(@PathVariable("idUsuario")Long idUsuario, @RequestBody PreguntaSeguridadDto dto) {
		return preguntaServ.cambiarPregunta(idUsuario, dto);
	}
	
}
