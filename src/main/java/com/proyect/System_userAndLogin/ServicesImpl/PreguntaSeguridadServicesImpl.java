package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IContrasenaReposiroty;
import com.proyect.System_userAndLogin.Repository.IPreguntaSeguridadRepository;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Services.IPreguntaSeguridadServices;

@Service
public class PreguntaSeguridadServicesImpl implements IPreguntaSeguridadServices{

	private static final Logger logger = LoggerFactory.getLogger(PreguntaSeguridadServicesImpl.class);
	
	private IPreguntaSeguridadRepository preguntaSeguridadRepository;
	private IUsuarioRepocitory usuarioRepository;
	
	private PreguntaSeguridadServicesImpl(IPreguntaSeguridadRepository preguntaSeguridadRepository, IUsuarioRepocitory usuarioRepository) {
		this.preguntaSeguridadRepository = preguntaSeguridadRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<PreguntaSeguridadResponseRest>  crearPregunta(PreguntaSeguridadDto preguntaSeguridad) {
		PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
		List<PreguntaSeguridadDto> preguntaSeguridadalist = new ArrayList<>();
		
		try {
	        Optional<Usuario> usuarioExist = usuarioRepository.findById(preguntaSeguridad.getIdUsuario());

		}catch (Exception e) {
		}
		
		return null;
	}

}
