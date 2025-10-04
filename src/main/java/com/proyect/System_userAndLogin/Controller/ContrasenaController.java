package com.proyect.System_userAndLogin.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.ActualizarContrasenaDto;
import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Response.ResponseContrasena.ContrasenaResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;

import jakarta.validation.Valid;


/**
  Controlador REST para operaciones relacionadas con contraseñas de usuarios.
 
  Expone endpoints para:
  
    Actualizar la contraseña validando la contraseña actual.
    Validar una contraseña en claro contra el hash persistido.
  
 
  Seguridad: Estos endpoints manejan datos sensibles.
  Asegura el uso de HTTPS, no registres contraseñas en logs y considera
  límites de intentos para evitar fuerza bruta.
 
  Ruta base:/v1/Contrasena
 
  Las respuestas siguen el contrato {@link ContrasenaResponseRest} con metadatos
  que indican éxito o error.
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
@RestController
@RequestMapping("/v1/Contrasena")
public class ContrasenaController {

    /** Servicio de negocio para la gestión de contraseñas. */
    private final IContrasenaServices contrasenaServices;

    /**
      Crea una instancia del controlador con su dependencia.
      @param contrasenaServices servicio de contraseñas
     */
    public ContrasenaController(IContrasenaServices contrasenaServices) {
        this.contrasenaServices = contrasenaServices;
    }

    /**
      Actualiza la contraseña de un usuario validando primero la contraseña actual.
     
      Flujo esperado:
      
        Verificar existencia de usuario y contraseña actual.
        Comparar la contraseña actual contra el hash almacenado.
        Hashear y persistir la nueva contraseña.
      
     
      @param idUsuario identificador del usuario
      @param actContra DTO con {@code contrasenaActual} y {@code contrasenaNueva} (se valida con Bean Validation)
      @return {@link ContrasenaResponseRest} con metadatos de operación (OK/NO OK)
     */
    @PutMapping("/actualizarContra/{idUsuario}")
    public ContrasenaResponseRest actualizaContra(
            @PathVariable("idUsuario") Long idUsuario,
            @Valid @RequestBody ActualizarContrasenaDto actContra) {
        return contrasenaServices.actualizarContrasena(idUsuario, actContra);
    }

    /**
      Valida una contraseña en claro contra el hash almacenado para el usuario.
     
      Seguridad: aunque este endpoint utiliza {@code POST}, el envío por
      {@code @RequestParam} puede aparecer en logs de acceso. Considera mover la contraseña
      a un cuerpo JSON ({@code @RequestBody}) para reducir exposición en trazas y proxies.
     
      @param idUsuario identificador del usuario
      @param contrasena contraseña en claro a validar
      @return {@link ContrasenaResponseRest} con metadatos: "OK" si válida, "NO OK" en caso contrario
     */
    @PostMapping("/validarContrasena/{idUsuario}")
    public ContrasenaResponseRest validarContrasena(
            @PathVariable("idUsuario") Long idUsuario,
            @RequestParam String contrasena) {
        return contrasenaServices.validarContrasena(idUsuario, contrasena);
    }
}