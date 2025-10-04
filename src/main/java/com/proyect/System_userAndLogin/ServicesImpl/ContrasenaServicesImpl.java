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
import org.springframework.transaction.annotation.Transactional;

import com.proyect.System_userAndLogin.Dto.ActualizarContrasenaDto;
import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Dto.PreguntaSeguridadDto;
import com.proyect.System_userAndLogin.Model.Contrasena;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IContrasenaReposiroty;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseRest;
import com.proyect.System_userAndLogin.Response.PreguntaSeguridadResponse.PreguntaSeguridadResponseRest;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;
import com.proyect.System_userAndLogin.Services.IPreguntaSeguridadServices;
import com.proyect.System_userAndLogin.Util.Util;

/**
  Implementación del servicio para gestión de contraseñas de usuarios.
 
  Responsabilidades:
  
    Crear la contraseña inicial de un usuario (hasheada).
    Actualizar contraseña verificando la actual.
    Crear/actualizar pregunta de seguridad y actualizar contraseña en un mismo flujo.
    Recuperar/establecer nueva contraseña validando por email y respuesta de seguridad.
    Validar una contraseña en claro contra el hash persistido.
  
 
  Seguridad: Todas las contraseñas se manejan como hash usando utilidades de
  cifrado/validación (p. ej. {@code Util.encriptarTexto} y {@code Util.verificarTexto}). No se debe
  loggear el valor de las contraseñas ni los hashes.
 
  Transaccionalidad: La clase está anotada con {@code @Transactional}; los métodos
  heredan este comportamiento salvo que se indique explícitamente lo contrario con {@code readOnly=true}.
 
  Respuestas:Se utilizan envoltorios tipo {@code ContrasenaResponseRest} con
  metadatos estándar (type, code, date/mensaje).
 
  Errores:La implementación captura excepciones y devuelve códigos/metadatos
  adecuados; se recomienda no exponer detalles internos en mensajes al cliente.
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */

@Service
@Transactional 
public class ContrasenaServicesImpl implements IContrasenaServices {

    private static final Logger logger = LoggerFactory.getLogger(ContrasenaServicesImpl.class);

    private final IContrasenaReposiroty contrasenaRepository;
    private final IUsuarioRepocitory usuarioRepository;
    private final IPreguntaSeguridadServices preguntaSeguridadServices;


    /**
      Crea una instancia del servicio de contraseñas con sus dependencias.
     
      @param contrasenaRepository repositorio de contraseñas (hash)
      @param usuarioRepository repositorio de usuarios
      @param preguntaSeguridadServices servicio para gestión de preguntas de seguridad
     */
    public ContrasenaServicesImpl(IContrasenaReposiroty contrasenaRepository,
                                  IUsuarioRepocitory usuarioRepository,
                                  IPreguntaSeguridadServices preguntaSeguridadServices) {
        this.contrasenaRepository = contrasenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.preguntaSeguridadServices = preguntaSeguridadServices;
    }

    /**
      Crea una contraseña para el usuario indicado, siempre en formato hash.
     
      Validaciones:
      
        El usuario debe existir.
        El usuario no debe tener ya una contraseña registrada.
      
     
      @param creaContrasena DTO con idUsuario y contraseña en claro (se hashea internamente)
      @return {@link ResponseEntity} con {@code 201 CREATED} si se creó, {@code 404 NOT_FOUND} si el usuario no existe,
              {@code 409 CONFLICT} si ya tenía contraseña, o {@code 500 INTERNAL_SERVER_ERROR} en error inesperado
     */
    @Override
    @Transactional
    public ResponseEntity<ContrasenaResponseRest> crearContrasena(ContrasenaDto creaContrasena) {
        ContrasenaResponseRest response = new ContrasenaResponseRest();
        List<ContrasenaDto> contrasenaList = new ArrayList<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(creaContrasena.getIdUsuario());
            if (!usuarioOpt.isPresent()) {
                response.setMetdata("NO OK", "-1", "Usuario con ID " + creaContrasena.getIdUsuario() + " no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Optional<Contrasena> existente = contrasenaRepository.findByUsuarioId(creaContrasena.getIdUsuario());
            if (existente.isPresent()) {
                response.setMetdata("NO OK", "-1", "El usuario ya tiene contraseña registrada");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            Contrasena contrasena = new Contrasena();
            String contrasenaHash = Util.encriptarTexto(creaContrasena.getContrasena()); 
            contrasena.setContrasena(contrasenaHash);
            contrasena.setUsuario(usuarioOpt.get());

            Contrasena guardada = contrasenaRepository.save(contrasena);

            ContrasenaDto respuestaDto = new ContrasenaDto();
            respuestaDto.setIdContrasena(guardada.getIdContrasena());
            respuestaDto.setIdUsuario(guardada.getUsuario().getIdUsuario());

            contrasenaList.add(respuestaDto);
            response.getContrasenaResponse().setContrasena(contrasenaList);
            response.setMetdata("OK", "1", "Contraseña guardada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error al guardar la contraseña: ", e);
            ContrasenaResponseRest err = new ContrasenaResponseRest();
            err.setMetdata("NO OK", "-1", "Error interno al guardar la contraseña");
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Actualiza la contraseña de un usuario validando la contraseña actual.
     
      Flujo:
      
        Valida existencia de usuario y de contraseña actual.
        Verifica la contraseña actual contra el hash.
        Hashea y guarda la nueva contraseña.
      
     
      @param idUsuario id del usuario
      @param actContra DTO con {@code contrasenaActual} y {@code contrasenaNueva} en claro
      @return {@code ContrasenaResponseRest} con metadatos de éxito o error (no usa HTTP wrapper aquí)
     */
    @Override
    @Transactional
    public ContrasenaResponseRest actualizarContrasena(Long idUsuario, ActualizarContrasenaDto actContra
    		//, String contrasenaActual, String contrasenaNueva
    		) {
        ContrasenaResponseRest response = new ContrasenaResponseRest();
        List<ContrasenaDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (!usuarioOpt.isPresent()) {
                response.setMetdata("nOK", "-1", "Usuario no encontrado");
                return response;
            }

            Optional<Contrasena> contrasenaOpt = contrasenaRepository.findByUsuarioId(idUsuario);
            if (!contrasenaOpt.isPresent()) {
                response.setMetdata("nOK", "-1", "No existe contraseña registrada para el usuario");
                return response;
            }

            Contrasena actual = contrasenaOpt.get();

            boolean ok = Util.verificarTexto(actContra.getContrasenaActual(), actual.getContrasena()); 
            if (!ok) {
                response.setMetdata("nOK", "-1", "Contraseña actual incorrecta");
                return response;
            }

            String nuevoHash = Util.encriptarTexto(actContra.getContrasenaNueva());
            actual.setContrasena(nuevoHash);
            contrasenaRepository.save(actual);

            ContrasenaDto dto = new ContrasenaDto();
            dto.setIdContrasena(actual.getIdContrasena());
            dto.setIdUsuario(idUsuario);
            lista.add(dto);

            response.getContrasenaResponse().setContrasena(lista);
            response.setMetdata("OK", "1", "Contraseña actualizada");
            return response;

        } catch (Exception e) {
            logger.error("Error al actualizar contraseña: ", e);
            response.setMetdata("nOK", "-1", "Error interno al actualizar contraseña");
            return response;
        }
    }

    /**
      Crea o actualiza la pregunta de seguridad y, en el mismo flujo,
      actualiza la contraseña del usuario.
     
     Notas:
      
        Si el usuario no tiene contraseña previa, solo se crea la pregunta y se retorna metadato adecuado.
        La nueva contraseña siempre se persiste como hash.
      
     
      @param idUsuario id del usuario
      @param dto DTO con pregunta y respuesta en claro (la respuesta se hashea internamente)
      @param nuevaContrasena nueva contraseña en claro (se hashea internamente)
      @return {@code ContrasenaResponseRest} con metadatos del resultado del proceso
     */
    @Override
    @Transactional
    public ContrasenaResponseRest crearPreguntaYActualizarContrasena(Long idUsuario,
                                                                     PreguntaSeguridadDto dto,
                                                                     String nuevaContrasena) {
        ContrasenaResponseRest response = new ContrasenaResponseRest();
        List<ContrasenaDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (!usuarioOpt.isPresent()) {
                response.setMetdata("NO OK", "-1", "Usuario no encontrado");
                return response;
            }

            preguntaSeguridadServices.crearOActualizar(
                idUsuario,
                dto.getPregunta(),
                dto.getRespuesta() 
            );

            Optional<Contrasena> contrasenaOpt = contrasenaRepository.findByUsuarioId(idUsuario);
            if (!contrasenaOpt.isPresent()) {
                response.setMetdata("OK", "1", "Pregunta creada; el usuario no tenía contraseña registrada");
                return response;
            }

            Contrasena actual = contrasenaOpt.get();
            String nuevoHash = Util.encriptarTexto(nuevaContrasena);
            actual.setContrasena(nuevoHash);
            contrasenaRepository.save(actual);

            ContrasenaDto r = new ContrasenaDto();
            r.setIdContrasena(actual.getIdContrasena());
            r.setIdUsuario(idUsuario);
            lista.add(r);
            response.getContrasenaResponse().setContrasena(lista);
            response.setMetdata("OK", "1", "Pregunta creada/actualizada y contraseña actualizada");
            return response;

        } catch (Exception e) {
            logger.error("Error en crearPreguntaYActualizarContrasena: ", e);
            response.setMetdata("NO OK", "-1", "Error interno");
            return response;
        }
    }

    /**
      Recupera/establece una nueva contraseña validando por email y respuesta de seguridad.
     
      Flujo:
      
        Busca el usuario por email.
        Valida la respuesta de seguridad vía servicio correspondiente.
        Si no hay contraseña previa, la crea; si existe, la actualiza.
      
     
      @param email correo del usuario
      @param respuesta respuesta a la pregunta de seguridad en claro
      @param nuevaContrasena nueva contraseña en claro (se hashea internamente)
      @return {@code ContrasenaResponseRest} con metadatos de éxito o error
     */
    @Override
    @Transactional
    public ContrasenaResponseRest recuperarContrasena(String email, String respuesta, String nuevaContrasena) {
        ContrasenaResponseRest response = new ContrasenaResponseRest();
        List<ContrasenaDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailIgnoreCase(email);
            if (!usuarioOpt.isPresent()) {
                response.setMetdata("NO OK", "-1", "Usuario no encontrado por email");
                return response;
            }
            Long idUsuario = usuarioOpt.get().getIdUsuario();

            ResponseRest validacion = preguntaSeguridadServices.validarRespuestaSeguridad(idUsuario, respuesta);
            String code;
            if (validacion.getMetdata().isEmpty()) {
                code = "-1";
            } else {
                HashMap<String, String> metadataMap = validacion.getMetdata().get(0);
                code = metadataMap.get("code"); 
            }

            if (!"1".equals(code)) {
                response.setMetdata("NO OK", "-1", "Respuesta de seguridad incorrecta");
                return response;
            }

            Optional<Contrasena> contrasenaOpt = contrasenaRepository.findByUsuarioId(idUsuario);
            if (!contrasenaOpt.isPresent()) {
                Contrasena nueva = new Contrasena();
                nueva.setUsuario(usuarioOpt.get());
                nueva.setContrasena(Util.encriptarTexto(nuevaContrasena));
                Contrasena guardada = contrasenaRepository.save(nueva);

                ContrasenaDto dto = new ContrasenaDto();
                dto.setIdContrasena(guardada.getIdContrasena());
                dto.setIdUsuario(idUsuario);
                lista.add(dto);
                response.getContrasenaResponse().setContrasena(lista);
                response.setMetdata("OK", "1", "Contraseña creada por recuperación");
                return response;
            }

            Contrasena actual = contrasenaOpt.get();
            actual.setContrasena(Util.encriptarTexto(nuevaContrasena));
            contrasenaRepository.save(actual);

            ContrasenaDto dto = new ContrasenaDto();
            dto.setIdContrasena(actual.getIdContrasena());
            dto.setIdUsuario(idUsuario);
            lista.add(dto);
            response.getContrasenaResponse().setContrasena(lista);
            response.setMetdata("OK", "1", "Contraseña actualizada por recuperación");
            return response;

        } catch (Exception e) {
            logger.error("Error en recuperarContrasena: ", e);
            response.setMetdata("NO OK", "-1", "Error interno");
            return response;
        }
    }

    /**
      Valida una contraseña en claro contra el hash almacenado para un usuario.
     
      @param idUsuario id del usuario
      @param contrasena contraseña en claro a verificar
      @return {@code ContrasenaResponseRest} con metadatos:
              "OK" si válida, "NO OK" si no corresponde o no existe registro
     */
    @Override
    @Transactional(readOnly = true)
    public ContrasenaResponseRest validarContrasena(Long idUsuario, String contrasena) {
        ContrasenaResponseRest response = new ContrasenaResponseRest();

        try {
            Optional<Contrasena> contrasenaOpt = contrasenaRepository.findByUsuarioId(idUsuario);
            logger.error(":::::::::::::::entra query::::::::::::::::::"+ idUsuario);
            System.err.println(":::::::::::::::entra contrasenaOpt.get()1::::::::::::::::::"+ contrasenaOpt.get().getContrasena());
            logger.error(":::::::::::::::entra contrasenaOpt.get()::::::::::::::::::"+ contrasenaOpt.get().toString());
            if (!contrasenaOpt.isPresent()) {
                response.setMetdata("NO OK", "-1", "No existe contraseña registrada para el usuario");
                return response;
            }

            boolean ok = Util.verificarTexto(contrasena, contrasenaOpt.get().getContrasena());
            if (!ok) {
                response.setMetdata("NO OK", "-1", "Contraseña incorrecta");
                return response;
            }

            response.setMetdata("OK", "1", "Contraseña válida");
            return response;

        } catch (Exception e) {
            logger.error("Error en validarContrasena: ", e);
            response.setMetdata("NO OK", "-1", "Error interno");
            return response;
        }
    }
}
