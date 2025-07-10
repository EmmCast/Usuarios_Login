package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IContrasenaReposiroty;
import com.proyect.System_userAndLogin.Repository.IPreguntaSeguridadRepository;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Services.IPreguntaSeguridadServices;
import com.proyect.System_userAndLogin.Util.Util;

import jakarta.transaction.Transactional;

@Service
public class PreguntaSeguridadServicesImpl implements IPreguntaSeguridadServices{

	private static final Logger logger = LoggerFactory.getLogger(PreguntaSeguridadServicesImpl.class);
	
	@Autowired
	private IPreguntaSeguridadRepository preguntaSeguridadRepository;
	
	@Autowired
	private IUsuarioRepocitory usuarioRepository;
	
    
    public PreguntaSeguridadServicesImpl(IPreguntaSeguridadRepository preguntaSeguridadRepository,
                                         IUsuarioRepocitory usuarioRepository) {
        this.preguntaSeguridadRepository = preguntaSeguridadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(PreguntaSeguridadDto preguntaSeguridad) {
        PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
        List<PreguntaSeguridadDto> listaDto = new ArrayList<>();

        try {
            Optional<Usuario> usuarioExist = usuarioRepository.findById(preguntaSeguridad.getUsuarioId());

            if (!usuarioExist.isPresent()) {
                response.setMetdata("Respuesta NO OK", "-1", "Usuario con ID " + preguntaSeguridad.getUsuarioId() + " no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }

            PreguntaSeguridad pregunta = new PreguntaSeguridad();
            String preguntaTexto = preguntaSeguridad.getPregunta(); // Puede quedar sin encriptar
            String respuestaHash = Util.encriptarTexto(preguntaSeguridad.getRespuesta());

            pregunta.setPregunta(preguntaTexto);
            pregunta.setRespuesta(respuestaHash);
            pregunta.setUsuarioId(usuarioExist.get());

            PreguntaSeguridad guardada = preguntaSeguridadRepository.save(pregunta);

            if (guardada.getIdPregunta() != null) {
                PreguntaSeguridadDto dto = new PreguntaSeguridadDto();
                dto.setIdPregunta(guardada.getIdPregunta());
                dto.setPregunta(guardada.getPregunta());
                dto.setRespuesta(guardada.getRespuesta());
                dto.setUsuarioId(guardada.getUsuarioId().getIdUsuario());

                listaDto.add(dto);
                response.getPreguntaSeguridadResponse().setPreguntaSeguridad(listaDto);
                response.setMetdata("Respuesta OK", "1", "Pregunta de seguridad guardada");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMetdata("Respuesta NO OK", "-1", "Pregunta de seguridad no guardada");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.error("Error al guardar la pregunta de seguridad", e);
            response.setMetdata("Respuesta NO OK", "-1", "Error interno al guardar la pregunta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
    @Override
    public PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario) {
        PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
        List<PreguntaSeguridadDto> lista = new ArrayList<>();

        try {
            Optional<PreguntaSeguridad> preguntaOpt = preguntaSeguridadRepository.findById(idUsuario);

            if (!preguntaOpt.isPresent()) {
                response.setMetdata("Respuesta NO OK", "-1", "No se encontró pregunta para el usuario con ID: " + idUsuario);
                return response;
            }

            PreguntaSeguridad pregunta = preguntaOpt.get();
            PreguntaSeguridadDto dto = new PreguntaSeguridadDto();

            dto.setIdPregunta(pregunta.getIdPregunta());
            dto.setPregunta(pregunta.getPregunta()); // solo la pregunta
            dto.setUsuarioId(pregunta.getUsuarioId().getIdUsuario());

            lista.add(dto);
            response.getPreguntaSeguridadResponse().setPreguntaSeguridad(lista);
            response.setMetdata("Respuesta OK", "1", "Pregunta recuperada exitosamente");
            return response;

        } catch (Exception e) {
            logger.error("Error al obtener la pregunta de seguridad", e);
            response.setMetdata("Respuesta NO OK", "-1", "Error al buscar pregunta");
            return response;
        }
    }

    @Override
    public ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta) {
        ResponseRest response = new ResponseRest();

        try {
            Optional<PreguntaSeguridad> preguntaOpt = preguntaSeguridadRepository.findById(idUsuario);

            if (!preguntaOpt.isPresent()) {
                response.setMetdata("Respuesta NO OK", "-1", "No se encontró pregunta para el usuario con ID: " + idUsuario);
                return response;
            }

            PreguntaSeguridad pregunta = preguntaOpt.get();
            boolean esValida = Util.verificarTexto(respuesta, pregunta.getRespuesta());

            if (esValida) {
                response.setMetdata("Respuesta OK", "1", "Respuesta correcta");
            } else {
                response.setMetdata("Respuesta NO OK", "-1", "Respuesta incorrecta");
            }

            return response;

        } catch (Exception e) {
            logger.error("Error al validar la respuesta de seguridad", e);
            response.setMetdata("Respuesta NO OK", "-1", "Error al validar respuesta");
            return response;
        }
    }


}
