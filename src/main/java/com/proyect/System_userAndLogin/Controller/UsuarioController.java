package com.proyect.System_userAndLogin.Controller;

import java.io.IOException;
import java.util.Set;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;
import com.proyect.System_userAndLogin.Util.Util;

import jakarta.validation.Valid;

/**
  Controlador REST para la gestión de usuarios.
 
  Expone endpoints para crear, listar, buscar, borrar (lógico/físico) y reactivar usuarios.
  Las respuestas siguen el contrato {@link UsuarioResponseRest} con metadatos estándar.
 
  Notas:
  
    Las validaciones de entrada (Bean Validation) deben definirse en {@link UsuarioDto}.
    Las operaciones que modifican estado delegan la lógica a {@link IUsuarioServices}.
    El endpoint {@code /saveUsuario} admite carga de imagen (multipart), comprimiendo bytes con {@link Util#compressZLib(byte[])}.
  
 
  Rutas base: {@code /v1/usuarios}
 
  Seguridad: configura acceso/roles desde tu {@code SecurityConfig} según el entorno (dev/prod).
 
  @author Emmanuel
  @version 1.5
  @since 2025-10

 **/

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
	
    /** Servicio de negocio para operaciones sobre usuarios. */
	@Autowired
	private IUsuarioServices usuarioServices;
	
	/*
	@PostMapping("/saveUsuario")
	public ResponseEntity<UsuarioResponseRest> saveUsuario (
			@RequestParam("primerNombre")String primerNombre,
			@RequestParam("segundoNombre")String segundoNombre,
			@RequestParam("apellidoPaterno")String apellidoPaterno,
			@RequestParam("apellidoMaterno")String apellidoMaterno,
			@RequestParam("email")String email,
			@RequestParam("telefono")String telefono,
			@RequestParam("fotoUsuario")MultipartFile  fotoUsuario,
			@RequestParam("rolId")Set<Long>  rolId	
			)throws IOException{
	
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setPrimerNombre(primerNombre);
		usuarioDto.setSegundoNombre(segundoNombre);
		usuarioDto.setApellidoPaterno(apellidoPaterno);
		usuarioDto.setApellidoMaterno(apellidoMaterno);
		usuarioDto.setTelefono(telefono);
		usuarioDto.setEmail(email);
		usuarioDto.setRolesIds(rolId);
		usuarioDto.setFotoUsuario(Util.compressZLib(fotoUsuario.getBytes()));
		System.err.println(usuarioDto.toString());
		ResponseEntity<UsuarioResponseRest> response = usuarioServices.guardarUsuario(usuarioDto);
		
		return response;
	}
	*/
	/*
	@PostMapping("/saveUsuario")
	public ResponseEntity<UsuarioResponseRest> saveUsuario (
			@RequestParam ("primerNombre")String primerNombre,
			@RequestParam("segundoNombre")String segundoNombre,
			@RequestParam("apellidoPaterno")String apellidoPaterno,
			@RequestParam("apellidoMaterno")String apellidoMaterno,
			@RequestParam("email")String email,
			@RequestParam("telefono")String telefono,
			@RequestParam("fotoUsuario")MultipartFile  fotoUsuario,
			@RequestParam("rolId")Set<Long>  rolId	
			)throws IOException{
	
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setPrimerNombre(primerNombre);
		usuarioDto.setSegundoNombre(segundoNombre);
		usuarioDto.setApellidoPaterno(apellidoPaterno);
		usuarioDto.setApellidoMaterno(apellidoMaterno);
		usuarioDto.setTelefono(telefono);
		usuarioDto.setEmail(email);
		usuarioDto.setRolesIds(rolId);
		usuarioDto.setFotoUsuario(Util.compressZLib(fotoUsuario.getBytes()));
		
		System.err.println(usuarioDto.toString());
		ResponseEntity<UsuarioResponseRest> response = usuarioServices.guardarUsuario(usuarioDto);
		
		return response;
	}
	*/
	
    /**
      Crea un usuario a partir de un payload JSON.
     
      Se recomienda enviar un {@link UsuarioDto} válido.
      El servicio asignará username, roles y contraseña inicial según su implementación.
     
      @param usuarioDto DTO con los datos del usuario
      @return {@link UsuarioResponseRest} con el usuario creado y metadatos
     */
    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseRest> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
        return usuarioServices.guardarUsuario(usuarioDto);
    }

    /**
      Crea un usuario mediante <em>multipart/form-data</em>, aceptando:
      <ul>
        <li>Parte {@code usuario}: objeto {@link UsuarioDto} serializado (JSON).</li>
        <li>Parte opcional {@code fotoUsuario}: archivo de imagen.</li>
      </ul>
     
      <p>Si se recibe imagen, se comprime con ZLIB antes de persistir.</p>
     
      @param usuarioDto parte nombrada {@code usuario} con el DTO
      @param fotoUsuario parte opcional con la imagen de perfil
      @return {@link UsuarioResponseRest} con el resultado de la creación
      @throws IOException si ocurre un error al leer los bytes del archivo
     */
    @PostMapping("/saveUsuario")
    public ResponseEntity<UsuarioResponseRest> saveUsuario(
            @Valid @RequestParam("usuario") UsuarioDto usuarioDto,
            @RequestPart(value = "fotoUsuario", required = false) MultipartFile fotoUsuario
    ) throws IOException {
        if (fotoUsuario != null && !fotoUsuario.isEmpty()) {
            usuarioDto.setFotoUsuario(Util.compressZLib(fotoUsuario.getBytes()));
        }
        return usuarioServices.guardarUsuario(usuarioDto);
    }

    /**
      Lista todos los usuarios activos.
     
      @return lista de usuarios activos en {@link UsuarioResponseRest}
     */
    @GetMapping("/listarUsuarios")
    public ResponseEntity<UsuarioResponseRest> ListarUsuarios() {
        return usuarioServices.buscarTodosActivos();
    }

    /**
      Lista todos los usuarios inactivos.
     
      @return lista de usuarios inactivos en {@link UsuarioResponseRest}
     */
    @GetMapping("/listarUsuariosInactivos")
    public ResponseEntity<UsuarioResponseRest> ListarUsuariosInactivos() {
        return usuarioServices.buscarTodosInactivos();
    }

    /**
      Busca un usuario activo por su identificador.
     
      @param idUsuario id del usuario
      @return usuario encontrado y metadatos; 404 si no existe/está inactivo
     */
    @GetMapping("/buscarUsuarioPorId/{idUsuario}")
    public ResponseEntity<UsuarioResponseRest> buscarUsuarioPorId(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioServices.buscarPorId(idUsuario);
    }

    /**
      Busca un usuario ACTIVO por su {@code username}.
     
      @param username nombre de usuario
      @return usuario encontrado y metadatos; 404 si no existe
     */
    @GetMapping("/buscarUsuarioPorUserName/{username}")
    public ResponseEntity<UsuarioResponseRest> buscarUsuarioPorUsername(@PathVariable("username") String username) {
        return usuarioServices.buscarPorUserNameActivo(username);
    }

    /**
      Borrado lógico (estado = FALSE) por id de usuario.
     
      @param idUsuario id del usuario
      @return resultado de la operación con metadatos
     */
    @PutMapping("/borradoLogicoPorIdUsuario/{idUsuario}")
    public ResponseEntity<UsuarioResponseRest> borradoLogicoPorId(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioServices.eliminadoLogicoUsuario(idUsuario);
    }

    /**
      Borrado lógico (estado = FALSE) por {@code username}.
     
      @param username nombre de usuario
      @return resultado de la operación con metadatos
     */
    @PutMapping("/borradoLogicoPorUsername/{username}")
    public ResponseEntity<UsuarioResponseRest> borradoLogicoPorUsername(@PathVariable("username") String username) {
        return usuarioServices.eliminarUsuarioLogicoPorUserName(username);
    }

    /**
      Borrado físico (DELETE) por id de usuario.
     
      @param idUsuario id del usuario
      @return resultado de la operación con metadatos
     */
    @DeleteMapping("/borradoFisicoPorId/{idUsuario}")
    public ResponseEntity<UsuarioResponseRest> borradoFisicoPorId(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioServices.eliminadiFisicoUsuario(idUsuario);
    }

    /**
      Borrado físico (DELETE) por {@code username}.
     
      @param username nombre de usuario
      @return resultado de la operación con metadatos
     */
    @DeleteMapping("/borradoFisicoPorUsername/{username}")
    public ResponseEntity<UsuarioResponseRest> borradoFisicoPorUsername(@PathVariable("username") String username) {
        return usuarioServices.eliminarUsuarioFisicoPorUserName(username);
    }

    /**
      Reactiva un usuario (estado = TRUE) por {@code username}.
     
      @param username nombre de usuario
      @return resultado de la operación con metadatos
     */
    @PutMapping("/reactivarUser/{username}")
    public ResponseEntity<UsuarioResponseRest> reactivarUser(@PathVariable("username") String username) {
        return usuarioServices.reactivarUsuariosPorUsername(username);
    }
}
