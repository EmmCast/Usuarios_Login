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

import jakarta.validation.Valid;

/**
  Controlador REST para la gestión de preguntas de seguridad de los usuarios.
 
  Expone endpoints para crear, verificar, obtener y actualizar la
  pregunta/respuesta de seguridad asociada a un usuario.
 
  Seguridad: la respuesta de seguridad debe manejarse siempre en texto
  claro solo a nivel de entrada; en la capa de servicio se hashea y se compara contra el hash
  almacenado. Evitar loggear valores sensibles.
 
  Ruta base:/v1/preguntaSeguridad
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
@RestController
@RequestMapping("/v1/preguntaSeguridad")
public class PreguntaSeguridadController {

    /** Servicio de negocio para preguntas de seguridad. */
    private final IPreguntaSeguridadServices preguntaServ;

    /**
      Crea una instancia del controlador con su dependencia.
      @param preguntaServ servicio para operaciones de pregunta/respuesta de seguridad
     */
    public PreguntaSeguridadController(IPreguntaSeguridadServices preguntaServ) {
        this.preguntaServ = preguntaServ;
    }

    /**
      Crea la pregunta de seguridad para un usuario.
     
      Si el usuario ya tenía una pregunta, la implementación devuelve conflicto (409).
      La respuesta del DTO se hashea en la capa de servicio antes de persistir.
     
      @param pregunta DTO con {@code usuarioId}, {@code pregunta} y {@code respuesta} en claro
      @return {@link ResponseEntity} con {@link PreguntaSeguridadResponseRest} y metadatos del resultado
     */
    @PostMapping("/preguntaSeguridad")
    public ResponseEntity<PreguntaSeguridadResponseRest> crearPregunta(
            @Valid @RequestBody PreguntaSeguridadDto pregunta) {
        return preguntaServ.crearPregunta(pregunta);
    }

    /**
      Verifica la respuesta de seguridad de un usuario contra el hash almacenado.
     
      Nota: el parámetro se recibe como @RequestParam("Respuesta")
      respetando la firma actual (sensible a mayúscula). Considera usar todo minúsculas
      (respuesta) por consistencia o moverlo a un cuerpo JSON para evitar que
      aparezca en logs de querystring.
     
      @param idUsuario identificador del usuario
      @param Respuesta respuesta en claro a validar
      @return {@link ResponseRest} con metadatos: "Respuesta OK" o "Respuesta NO OK"
     */
    @PostMapping("/verificarPregunta/{idUsuario}")
    public ResponseRest verificarPregunta(
            @PathVariable("idUsuario") Long idUsuario,
            @RequestParam("Respuesta") String Respuesta) {
        return preguntaServ.validarRespuestaSeguridad(idUsuario, Respuesta);
    }

    /**
      Obtiene la pregunta de seguridad asociada a un usuario por su id.
     
      No expone el hash de la respuesta; solo devuelve la pregunta y el {@code usuarioId}.
     
      @param idUsuario identificador del usuario
      @return {@link PreguntaSeguridadResponseRest} con la pregunta y metadatos
     */
    @GetMapping("/obtenerPreguntaporIdUsuario/{idUsuario}")
    public PreguntaSeguridadResponseRest obtenerpreguntaPorIdUsuario(
            @PathVariable("idUsuario") Long idUsuario) {
        return preguntaServ.obtenerPorUsuarioId(idUsuario);
    }

    /**
      Cambia la pregunta y la respuesta de seguridad de un usuario.
     
      La implementación hashea la nueva respuesta antes de persistirla. Si el registro
      no existe, se devuelve un estado acorde desde la capa de servicio.
     
      @param idUsuario identificador del usuario
      @param dto DTO con la nueva {@code pregunta} y {@code respuesta} en claro
      @return {@link ResponseEntity} con {@link PreguntaSeguridadResponseRest} y metadatos del resultado
     */
    @PutMapping("/cambiarPregunta/{idUsuario}")
    public ResponseEntity<PreguntaSeguridadResponseRest> actualizar(
            @PathVariable("idUsuario") Long idUsuario,
            @Valid @RequestBody PreguntaSeguridadDto dto) {
        return preguntaServ.cambiarPregunta(idUsuario, dto);
    }
}