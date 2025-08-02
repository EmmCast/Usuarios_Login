package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Model.Contrasena;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IContrasenaReposiroty;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;
import com.proyect.System_userAndLogin.Util.Util;

@Service
public class ContrasenaServicesImpl implements IContrasenaServices{

	private static final Logger logger = LoggerFactory.getLogger(ContrasenaServicesImpl.class);
	
	private IContrasenaReposiroty contrasenaRepository;
	private IUsuarioRepocitory usuarioRepository;
	
	private ContrasenaServicesImpl(IContrasenaReposiroty contrasenaRepository, IUsuarioRepocitory usuarioRepository) {
		this.contrasenaRepository = contrasenaRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public ResponseEntity<ContrasenaResponseRest> crearContrasena(ContrasenaDto creaContrasena) {
		 ContrasenaResponseRest response = new ContrasenaResponseRest();
		    List<ContrasenaDto> contrasenaList = new ArrayList<>();

		    try {
		        Optional<Usuario> usuarioExist = usuarioRepository.findById(creaContrasena.getIdUsuario());

		        if (!usuarioExist.isPresent()) {
		            response.setMetdata("Respuesta NO OK", "-1", "Usuario con ID " + creaContrasena.getIdUsuario() + " no encontrado");
		            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		        }

		        Contrasena contrasena = new Contrasena();
		        String contrasenaHash = Util.encriptarTexto(creaContrasena.getContrasena());

		        contrasena.getIdContrasena();
		        contrasena.setContrasena(contrasenaHash);
		        contrasena.setUsuario(usuarioExist.get());

		        Contrasena contrasenaGuardada = contrasenaRepository.save(contrasena);

		        if (contrasenaGuardada.getIdContrasena() != null) {
		            ContrasenaDto respuestaDto = new ContrasenaDto();
		            respuestaDto.setIdContrasena(contrasenaGuardada.getIdContrasena());
		            respuestaDto .setIdUsuario(contrasenaGuardada.getUsuario().getIdUsuario());

		            contrasenaList.add(respuestaDto);
		            response.getContrasenaResponse().setContrasena(contrasenaList);
		            response.setMetdata("Respuesta OK", "1", "Contraseña guardada exitosamente");
		            return new ResponseEntity<>(response, HttpStatus.CREATED);
		        } else {
		            response.setMetdata("Respuesta NO OK", "-1", "Contraseña no guardada");
		            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		        }

		    } catch (Exception e) {
		        logger.error("Error al guardar la contraseña: ", e);
		        response.setMetdata("Respuesta nOK", "-1", " Error interno al guardar la contraseña");
		        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

	@Override
	public PreguntaSeguridadResponseRest actualizarContrasena(Long idUsuario, String contrasenaActual,
			String contrasenaNueva) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreguntaSeguridadResponseRest crearPreguntaYActualizarContrasena(Long idUsuario, PreguntaSeguridadDto dto,
			String nuevaContrasena) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreguntaSeguridadResponseRest recuperarContrasena(String email, String respuesta, String nuevaContrasena) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
