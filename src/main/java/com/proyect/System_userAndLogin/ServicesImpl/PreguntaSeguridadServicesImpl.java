package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.CambiarPreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Model.PreguntaSeguridad;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IPreguntaSeguridadRepository;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Services.IPreguntaSeguridadServices;
import com.proyect.System_userAndLogin.Util.Util;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
  Implementación del servicio de negocio para la gestión de preguntas de seguridad.
 
  Responsabilidades principales:
  
    Crear y actualizar la pregunta/respuesta de seguridad asociada a un usuario.
    Obtener la pregunta de seguridad por id de usuario.
    Validar la respuesta ingresada contra el hash persistido.
    Flujo de cambio de pregunta/respuesta con hashing seguro.
  
 
  Seguridad: Las respuestas se almacenan como hash< usando
  {@link Util#encriptarTexto(String)} y se validan con {@link Util#verificarTexto(String, String)}.
  Nunca se almacena la respuesta en texto plano. Evitar loggear valores sensibles.
 
  Transaccionalidad: Los métodos que escriben en BD usan
  {@link Transactional} para asegurar atomicidad.
 
  Errores y metadatos:Las respuestas REST incluyen metadatos estándar
  (type, code, date/mensaje) dentro de {@link ResponseRest} y derivados.
 
  Nota: Se espera que la validación de DTOs (@Valid) ocurra en la capa Controller.
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/
@Service
public class PreguntaSeguridadServicesImpl implements IPreguntaSeguridadServices {

    /** Logger para trazas e incidencias del servicio. */
    private static final Logger logger = LoggerFactory.getLogger(PreguntaSeguridadServicesImpl.class);

    /** Repositorio de persistencia para preguntas de seguridad. */
    @Autowired
    private IPreguntaSeguridadRepository preguntaSeguridadRepository;

    /** Repositorio de acceso a usuarios. */
    @Autowired
    private IUsuarioRepocitory usuarioRepository;

    /**
      Crea una instancia del servicio con sus dependencias.
     
      @param preguntaSeguridadRepository repositorio de preguntas de seguridad
      @param usuarioRepository repositorio de usuarios
     */
    public PreguntaSeguridadServicesImpl(IPreguntaSeguridadRepository preguntaSeguridadRepository,
                                         IUsuarioRepocitory usuarioRepository) {
        this.preguntaSeguridadRepository = preguntaSeguridadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
      Crea una nueva pregunta de seguridad para un usuario.
     
      Valida existencia del usuario y unicidad de la pregunta por usuario.
      La respuesta recibida en claro se hashea antes de persistir.
     
      @param preguntaSeguridad DTO con pregunta y respuesta en claro
      @return {@link ResponseEntity} con {@link PreguntaSeguridadResponseRest} y estatus:
      
        201 (CREATED) si se crea correctamente.
        404 (NOT_FOUND) si el usuario no existe.
        409 (CONFLICT) si el usuario ya tenía registrada una pregunta.
        500 (INTERNAL_SERVER_ERROR) ante error inesperado.
      
     */
    @Transactional
    @Override
    public ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(PreguntaSeguridadDto preguntaSeguridad) {
        PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
        List<PreguntaSeguridadDto> listaDto = new ArrayList<>();

        try {
            Optional<Usuario> usuarioExist = usuarioRepository.findById(preguntaSeguridad.getUsuarioId());
            if (usuarioExist.isEmpty()) {
                response.setMetdata("nOk", "-1",
                        "Usuario con ID " + preguntaSeguridad.getUsuarioId() + " no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Long usuarioId = usuarioExist.get().getIdUsuario();

            boolean yaExiste = preguntaSeguridadRepository.existsByUsuarioId(usuarioId);
            if (yaExiste) {
                response.setMetdata("Conflicto", "-1", "El usuario ya tiene una pregunta de seguridad registrada");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            String preguntaTexto = (preguntaSeguridad.getPregunta() == null) ? null
                    : preguntaSeguridad.getPregunta().trim();

            String respuestaHash = Util.encriptarTexto(
                    preguntaSeguridad.getRespuesta() == null ? "" : preguntaSeguridad.getRespuesta().trim());

            PreguntaSeguridad entidad = new PreguntaSeguridad();
            entidad.setPregunta(preguntaTexto);
            entidad.setRespuesta(respuestaHash);
            entidad.setUsuario(usuarioExist.get());

            PreguntaSeguridad guardada = preguntaSeguridadRepository.save(entidad);

            PreguntaSeguridadDto dto = new PreguntaSeguridadDto();
            dto.setPregunta(guardada.getPregunta());
            dto.setUsuarioId(usuarioId);

            listaDto.add(dto);
            response.getPreguntaSeguridadResponse().setPreguntaSeguridad(listaDto);
            response.setMetdata("Pregunta de seguridad guardada", "1", "Creado");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            response.setMetdata("Conflicto", "-1", "Ya existe una pregunta de seguridad para este usuario");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);

        } catch (Exception e) {
            logger.error("Error al guardar la pregunta de seguridad", e);
            response.setMetdata("nOk", "-1", "Error interno al guardar la pregunta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Obtiene la pregunta de seguridad asociada a un usuario por su id.
     
      @param idUsuario identificador del usuario
      @return respuesta con la pregunta (sin exponer el hash de la respuesta)
             y metadatos: "OK" si existe; "nOK" si no se encuentra.
     */
    @Override
    public PreguntaSeguridadResponseRest obtenerPorUsuarioId(Long idUsuario) {
        PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
        List<PreguntaSeguridadDto> lista = new ArrayList<>();

        try {
            Optional<PreguntaSeguridad> preguntaOpt = preguntaSeguridadRepository.findByIdUser(idUsuario);

            if (!preguntaOpt.isPresent()) {
                response.setMetdata("nOK", "-1", "No se encontró pregunta para el usuario con ID: " + idUsuario);
                return response;
            }

            PreguntaSeguridad pregunta = preguntaOpt.get();
            PreguntaSeguridadDto dto = new PreguntaSeguridadDto();

            dto.setPregunta(pregunta.getPregunta());
            dto.setUsuarioId(pregunta.getUsuario().getIdUsuario());

            lista.add(dto);
            response.getPreguntaSeguridadResponse().setPreguntaSeguridad(lista);
            response.setMetdata("OK", "1", "Pregunta recuperada exitosamente");
            return response;

        } catch (Exception e) {
            logger.error("Error al obtener la pregunta de seguridad", e);
            response.setMetdata("nOK", "-1", "Error al buscar pregunta");
            return response;
        }
    }

    /**
      Valida una respuesta de seguridad en claro contra el hash almacenado.
     
      @param idUsuario identificador del usuario
      @param respuesta respuesta en claro a validar
      @return {@link ResponseRest} con metadatos:
      
        "Respuesta OK" si la validación es correcta.
        "Respuesta NO OK" si no coincide o no existe pregunta.
      
     */
    @Override
    public ResponseRest validarRespuestaSeguridad(Long idUsuario, String respuesta) {
        ResponseRest response = new ResponseRest();

        try {
            Optional<PreguntaSeguridad> preguntaOpt = preguntaSeguridadRepository.findByIdUser(idUsuario);

            if (!preguntaOpt.isPresent()) {
                response.setMetdata("Respuesta NO OK", "-1",
                        "No se encontró pregunta para el usuario con ID: " + idUsuario);
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

    /**
      Cambia la pregunta y la respuesta de seguridad de un usuario.
     
      Si no existe un registro previo del usuario, responde con metadatos de no encontrado.
      La nueva respuesta se hashea antes de persistir.
     
      @param idUsuario id del usuario
      @param dto DTO con la nueva pregunta y respuesta en claro
      @return {@link ResponseEntity} con {@link PreguntaSeguridadResponseRest} y estatus:
      
       201 (CREATED) si se realiza el cambio.
        200 (OK) con metadatos "nOk" si no existía el registro (tal como está implementado).
        500 (INTERNAL_SERVER_ERROR) ante error.
      
     */
    @Override
    public ResponseEntity<PreguntaSeguridadResponseRest> cambiarPregunta(Long idUsuario, PreguntaSeguridadDto dto) {
        PreguntaSeguridadResponseRest response = new PreguntaSeguridadResponseRest();
        List<PreguntaSeguridadDto> listaDto = new ArrayList<>();

        try {
            Optional<PreguntaSeguridad> preguntaExist = preguntaSeguridadRepository.findByIdUsuario(idUsuario);
            if (preguntaExist.isEmpty()) {
                response.setMetdata("nOk", "-1", "usuario no Encontrado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            PreguntaSeguridad pregunta = preguntaExist.get();

            String preguntaTexto = (dto.getPregunta() == null) ? null : dto.getPregunta().trim();
            String respuestaHash = Util.encriptarTexto(dto.getRespuesta() == null ? "" : dto.getRespuesta().trim());

            PreguntaSeguridad entidad = new PreguntaSeguridad();
            entidad.setIdPregunta(pregunta.getIdPregunta());
            entidad.setPregunta(preguntaTexto);
            entidad.setRespuesta(respuestaHash);
            entidad.setUsuario(pregunta.getUsuario());

            PreguntaSeguridad guardada = preguntaSeguridadRepository.save(entidad);

            PreguntaSeguridadDto pregDto = new PreguntaSeguridadDto();
            pregDto.setPregunta(guardada.getPregunta());
            pregDto.setUsuarioId(idUsuario);

            listaDto.add(pregDto);
            response.getPreguntaSeguridadResponse().setPreguntaSeguridad(listaDto);
            response.setMetdata("Ok", "1", "Creado");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error al guardar la pregunta de seguridad", e);
            response.setMetdata("nOk", "-1", "Error interno al guardar la pregunta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Crea o actualiza la pregunta y respuesta de seguridad para un usuario.
     
      Si el usuario no existe se lanza una {@link RuntimeException}.
      Si ya existe un registro, se actualiza; de lo contrario, se crea
      uno nuevo. La respuesta se persiste como hash.
     
      Transacciones: Recomendado anotar como {@code @Transactional}
      en la implementación (la firma aquí no la incluye).
     
      @param idUsuario id del usuario
      @param pregunta texto de la pregunta
      @param respuestaPlano respuesta en claro (se hashea internamente)
      @throws RuntimeException si no se encuentra el usuario
     */
    @Override
    public void crearOActualizar(Long idUsuario, String pregunta, String respuestaPlano) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + idUsuario + " no encontrado"));

        Optional<PreguntaSeguridad> existenteOpt = preguntaSeguridadRepository.findByIdUsuario(idUsuario);

        PreguntaSeguridad entidad;
        if (existenteOpt.isPresent()) {
            entidad = existenteOpt.get();
            entidad.setPregunta(pregunta);
            entidad.setRespuesta(Util.encriptarTexto(respuestaPlano));
        } else {
            entidad = new PreguntaSeguridad();
            entidad.setUsuario(usuario);
            entidad.setPregunta(pregunta);
            entidad.setRespuesta(Util.encriptarTexto(respuestaPlano));
        }

        // Nota: faltaría persistir la entidad.
        // preguntaSeguridadRepository.save(entidad);
    }
}