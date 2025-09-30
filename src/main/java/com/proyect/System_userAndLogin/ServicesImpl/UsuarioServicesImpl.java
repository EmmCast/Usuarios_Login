package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.ContrasenaDto;
import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Model.FotoUsuario;
import com.proyect.System_userAndLogin.Model.Rol;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;
import com.proyect.System_userAndLogin.Services.IFotoUsuarioServices;
import com.proyect.System_userAndLogin.Services.IRolServices;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServicesImpl implements IUsuarioServices{

 //   private final ContrasenaServicesImpl contrasenaServicesImpl;

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicesImpl.class);
	
	private final IUsuarioRepocitory usuarioRepository;
	
	private final IRolServices rolServices;
	
	private final IContrasenaServices contrasenaServices;
	
	private final IFotoUsuarioServices fotoUsuarioServices;
	
	private final userNameServicesImpl userNameServ;
	
/*	
	@Autowired
	public UsuarioServicesImpl (IUsuarioRepocitory usuarioRepository,IRolServices rolServices,
			IContrasenaServices contrasenaServices, @Lazy IFotoUsuarioServices fotoUsuarioServices) {
		this.usuarioRepository = usuarioRepository;
		this.rolServices = rolServices;
		this.contrasenaServices = contrasenaServices;
		this.fotoUsuarioServices = fotoUsuarioServices;
	}
*/
	
	@Autowired
	public UsuarioServicesImpl (IUsuarioRepocitory usuarioRepository,
								userNameServicesImpl userNameServ,
	                            IRolServices rolServices,
	                            IContrasenaServices contrasenaServices,
	                            @Lazy IFotoUsuarioServices fotoUsuarioServices, ContrasenaServicesImpl contrasenaServicesImpl) {
	    this.usuarioRepository = usuarioRepository;
	    this.userNameServ = userNameServ;
	    this.rolServices = rolServices;
	    this.contrasenaServices = contrasenaServices;
	    this.fotoUsuarioServices = fotoUsuarioServices;
//	    this.contrasenaServicesImpl = contrasenaServicesImpl;
	}
	
	private UsuarioDto mapperUsuarioDto(Usuario usuario) {
		UsuarioDto dto = new UsuarioDto();
		dto.setIdUsuario(usuario.getIdUsuario());
		
		dto.setNombreUsuario(usuario.getNombreUsuario());
		dto.setPrimerNombre(usuario.getPrimerNombre());
		dto.setSegundoNombre(usuario.getSegundoNombre());
		dto.setApellidoPaterno(usuario.getApellidoPaterno());
		dto.setApellidoMaterno(usuario.getApellidoMaterno());
		dto.setEmail(usuario.getEmail());
		dto.setTelefono(usuario.getTelefono());
		
		dto.setFechaIngreso(usuario.getFechaIngreso());
		
		if (usuario.getFotoUsuario() != null) {
		    dto.setFotoUsuario(usuario.getFotoUsuario().getFoto());
		}
/*
		Set<Rol> roles = dto.getRolesIds().stream()
	            .map(rolServices::buscarPorId)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());

	        usuario.setRoles(roles);
	*/
	    Set<Long> rolesIds = (usuario.getRoles() == null) ? java.util.Set.of()
	            : usuario.getRoles().stream().map(Rol::getIdRol).collect(java.util.stream.Collectors.toSet());
	    dto.setRolesIds(rolesIds);
	/*    
	    Set<String> rolesNombres = (usuario.getRoles()==null)? Set.of()
	    	    : usuario.getRoles().stream().map(Rol::getRol).collect(Collectors.toSet());
	    	dto.setRoles(rolesNombres);
	  */  
		return dto;
	}
	
	@Transactional
	public Usuario crearUsuario(Usuario nuevo) {
	    int reintentos = 0;
	    while (true) {
	        try {
	            String username = userNameServ.generarUsuario(nuevo.getPrimerNombre(),nuevo.getApellidoPaterno());
	            nuevo.setNombreUsuario(username);
	            return usuarioRepository.save(nuevo);
	        } catch (DataIntegrityViolationException ex) {
	            if (++reintentos > 5) throw ex;
	        }
	    }
	}
	
	@Override
	@Transactional
	public ResponseEntity<UsuarioResponseRest> guardarUsuario(UsuarioDto dto) {
		  UsuarioResponseRest response = new UsuarioResponseRest();
		    List<UsuarioDto> lista = new ArrayList<>();

		    try {
		        Usuario usuario = new Usuario();
		        usuario.getIdUsuario();
		        usuario.setPrimerNombre(dto.getPrimerNombre());
		        usuario.setSegundoNombre(dto.getSegundoNombre());
		        usuario.setApellidoPaterno(dto.getApellidoPaterno());
		        usuario.setApellidoMaterno(dto.getApellidoMaterno());
		        usuario.setEmail(dto.getEmail());
		        usuario.setTelefono(dto.getTelefono());
		        usuario.getFechaIngreso();
		        usuario.getUpdated_at();
//		        usuario.setNombreUsuario(dto.getPrimerNombre().concat(dto.getApellidoMaterno()));
		        usuario.setEstado(true);

		        // Asignar roles desde ids
		        Set<Rol> roles = dto.getRolesIds().stream()
			            .map(rolServices::buscarPorId)
			            .filter(Objects::nonNull)
			            .collect(Collectors.toSet());

		        usuario.setRoles(roles);

		        int reintentos = 0;
	            Usuario usuarioGuardadoU = null;
	            while (true) {
	                try {
	                    String username =  userNameServ.generarUsuario(
	                            dto.getPrimerNombre(), dto.getApellidoPaterno());
	                    usuario.setNombreUsuario(username);
	                    usuarioGuardadoU = usuarioRepository.saveAndFlush(usuario); 
	                    break;
	                } catch (org.springframework.dao.DataIntegrityViolationException ex) {
	                    Throwable root = org.springframework.core.NestedExceptionUtils.getMostSpecificCause(ex);
	                    String msg = root != null ? root.getMessage() : ex.getMessage();
	                    if (msg != null && msg.toLowerCase().contains("nombre_usuario")) {
	                        if (++reintentos <= 5) {
	                            logger.warn("Choque de username, reintentando (intento {})", reintentos);
	                            continue;
	                        }
	                    }
	                    response.setMetdata("Conflicto", "409", "Datos duplicados: " + msg);
	                    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	                }
	            }
		        
		        Usuario usuarioGuardado = usuarioRepository.save(usuario);

		        ContrasenaDto contrasena = new ContrasenaDto();
		        
		        contrasena.setIdUsuario(usuarioGuardado.getIdUsuario());
		        contrasena.setContrasena(dto.getPrimerNombre().concat(".").concat(dto.getApellidoPaterno()));
		        
		        contrasenaServices.crearContrasena(contrasena);
		        
		        if (dto.getFotoUsuario() != null) {
		        	fotoUsuarioServices.guardarFoto(usuarioGuardado.getIdUsuario(), dto.getFotoUsuario());
		        }

		        UsuarioDto dtoFinal = mapperUsuarioDto(usuarioGuardado);
		        lista.add(dtoFinal);
		        response.getUsuarioResponse().setUsuario(lista);
		        response.setMetdata("OK", "1", "Usuario guardado correctamente");
		        return new ResponseEntity<>(response, HttpStatus.CREATED);

		    } catch (Exception e) {
		        logger.error("Error al guardar usuario", e);
		        response.setMetdata("nOk", "-1", "No se pudo guardar el usuario");
		        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
	@Override
	@Transactional()
	public ResponseEntity<UsuarioResponseRest> buscarPorId(Long idUsuario) {
	    UsuarioResponseRest response = new UsuarioResponseRest();
	    List<UsuarioDto> lista = new ArrayList<>();
	    try {
	        Optional<Usuario> usuarioOpt = usuarioRepository.findByIdActivo(idUsuario);

	        if (usuarioOpt.isEmpty()) {
	            response.setMetdata("nOk", "-1", "Usuario no encontrado");
	            // 404 con body (tu cliente verá el JSON)
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        Usuario usuario = usuarioOpt.get();
	        UsuarioDto dto = mapperUsuarioDto(usuario);

	        // Mejor usa el id del propio objeto por seguridad
	        FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
	        if (foto != null) {
	            dto.setFotoUsuario(foto.getFoto());
	        }

	        lista.add(dto);
	        response.getUsuarioResponse().setUsuario(lista);
	        response.setMetdata("OK", "1", "Usuario encontrado");

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("Error al buscar usuario por Id {}", idUsuario, e);
	        response.setMetdata("nOk", "-1", "Error interno al buscar el usuario");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@Override
	public ResponseEntity<UsuarioResponseRest> buscarPorUserNameActivo(String username) {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		try {
			Optional<Usuario> usuarioOptional = usuarioRepository.findUsername(username);
			if(usuarioOptional.isEmpty()) {
				response.setMetdata("nOk", "-1", "Usuario No encontrado");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			
			Usuario usuario = usuarioOptional.get();
			UsuarioDto dto = mapperUsuarioDto(usuario);
			
			FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
			if(foto != null) {
				dto.setFotoUsuario(foto.getFoto());
			}
			
			lista.add(dto);
			response.getUsuarioResponse().setUsuario(lista);
			response.setMetdata("Ok", "1", "Usuario encontrado");
			return ResponseEntity.ok(response);
		}catch (Exception e) {
	        logger.error("Error al buscar usuario por Id {}", username, e);
	        response.setMetdata("nOk", "-1", "Error interno al buscar el usuario");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@Override
	public ResponseEntity<UsuarioResponseRest> buscarTodosActivos() {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		
		try {
			List<Usuario> usuarios = usuarioRepository.findAllActivos();
			
			for(Usuario usuario : usuarios) {
				UsuarioDto dto = mapperUsuarioDto(usuario);
				FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
				if(foto != null) {
					dto.setFotoUsuario(foto.getFoto());
				}
				lista.add(dto);
			}
			response.getUsuarioResponse().setUsuario(lista);
			response.setMetdata("Ok", "1", "Usuarios Encontrados");
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			logger.error("Error al buscar a todos los usuarios", e);
			response.setMetdata(" nOk", "-1", "Error al buscar los usuarios");
			return new ResponseEntity<> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public ResponseEntity<UsuarioResponseRest> buscarTodosInactivos() {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		
		try {
			List<Usuario> usuarios = usuarioRepository.findAllInactivos();
			
			for(Usuario usuario : usuarios) {
				UsuarioDto dto = mapperUsuarioDto(usuario);
				FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
				if(foto != null) {
					dto.setFotoUsuario(foto.getFoto());
				}
				lista.add(dto);
			}
			response.getUsuarioResponse().setUsuario(lista);
			response.setMetdata("Ok", "1", "Usuarios Encontrados");
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			logger.error("Error al buscar a todos los usuarios", e);
			response.setMetdata("nOk", "-1", "Error al buscar los usuarios");
			return new ResponseEntity<> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<UsuarioResponseRest> actualizarUsuario(Long idUsuario, UsuarioDto dto) {
	    UsuarioResponseRest response = new UsuarioResponseRest();
	    List<UsuarioDto> lista = new ArrayList<>();

	    try {
	        Optional<Usuario> usuarioOptional = usuarioRepository.findByIdActivo(idUsuario);

	        if (!usuarioOptional.isPresent()) {
	            response.setMetdata("nOk", "-1", "Usuario no encontrado");
	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	        }

	        Usuario usuario = usuarioOptional.get();
	        usuario.setEmail(dto.getEmail());
	        usuario.setTelefono(dto.getTelefono());

	        Set<Rol> roles = dto.getRolesIds().stream()
		            .map(rolServices::buscarPorId)
		            .filter(Objects::nonNull)
		            .collect(Collectors.toSet());

	        usuario.setRoles(roles);

	        Usuario usuarioActualizado = usuarioRepository.save(usuario);

	        if (dto.getFotoUsuario() != null) {
	        	fotoUsuarioServices.actualizarFoto(idUsuario, dto.getFotoUsuario());
	        }

	        UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
	        lista.add(dtoFinal);
	        response.getUsuarioResponse().setUsuario(lista);
	        response.setMetdata("OK", "1", "Usuario actualizado correctamente");
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("Error al actualizar usuario", e);
	        response.setMetdata("nOk", "-1", "No se pudo actualizar el usuario");
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@Override
	public ResponseEntity<UsuarioResponseRest> eliminadoLogicoUsuario(Long idUsuario) {
		  UsuarioResponseRest response = new UsuarioResponseRest();
		    List<UsuarioDto> lista = new ArrayList<>();

		    try {
		        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

		        if (!usuarioOptional.isPresent()) {
		            response.setMetdata("nOk", "-1", "Usuario no encontrado");
		            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		        }

		        Usuario usuario = usuarioOptional.get();
		        usuario.setEstado(false);


		        Usuario usuarioActualizado = usuarioRepository.save(usuario);

		        UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
		        lista.add(dtoFinal);
		        response.getUsuarioResponse().setUsuario(lista);
		        response.setMetdata("OK", "1", "Usuario Eliminado Logicamente correctamente");
		        return new ResponseEntity<>(response, HttpStatus.OK);

		    } catch (Exception e) {
		        logger.error("Error al actualizar usuario", e);
		        response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
		        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}

	@Override
	public ResponseEntity<UsuarioResponseRest> eliminarUsuarioLogicoPorUserName(String userName) {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		
		try {
			Optional<Usuario> usuarioExist = usuarioRepository.findUsername(userName);
			if(usuarioExist.isEmpty()) {
				response.setMetdata("nOk ", "-1", " Usuario no encontrado");
				return new ResponseEntity<UsuarioResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			Usuario usuario = usuarioExist.get();
			usuario.setEstado(false);
			
			Usuario usuarioActualizado = usuarioRepository.save(usuario);
			
			UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
	        lista.add(dtoFinal);
	        response.getUsuarioResponse().setUsuario(lista);
	        response.setMetdata("OK", "1", "Usuario Eliminado Logicamente correctamente");
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("Error al actualizar usuario", e);
	        response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@Override
	public ResponseEntity<UsuarioResponseRest> eliminadiFisicoUsuario(Long idUsuario) {
		  UsuarioResponseRest response = new UsuarioResponseRest();
		    List<UsuarioDto> lista = new ArrayList<>();

		    try {
		        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

		        if (!usuarioOptional.isPresent()) {
		            response.setMetdata("nOk", "-1", "Usuario no encontrado");
		            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		        }

		        Usuario usuario = usuarioOptional.get();

		        usuarioRepository.deleteById(usuario.getIdUsuario());

//		        UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
//		        lista.add(dtoFinal);
		        response.getUsuarioResponse().setUsuario(lista);
		        response.setMetdata("OK", "1", "Usuario Eliminado Permanente correctamente");
		        return new ResponseEntity<>(response, HttpStatus.OK);

		    } catch (Exception e) {
		        logger.error("Error al actualizar usuario", e);
		        response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
		        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}

	@Override
	public ResponseEntity<UsuarioResponseRest> eliminarUsuarioFisicoPorUserName(String userName) {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		
		try {
			Optional<Usuario> usuarioExist = usuarioRepository.findUsername(userName);
			if(usuarioExist.isEmpty()) {
				response.setMetdata("nOk ", "-1", " Usuario no encontrado");
				return new ResponseEntity<UsuarioResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
	        Usuario usuario = usuarioExist.get();

	        usuarioRepository.deleteById(usuario.getIdUsuario());
			
//			UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
//	        lista.add(dtoFinal);
	        response.getUsuarioResponse().setUsuario(lista);
	        response.setMetdata("OK", "1", "Usuario Eliminado Logicamente correctamente");
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("Error al actualizar usuario", e);
	        response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<UsuarioResponseRest> reactivarUsuariosPorUsername(String username) {
		UsuarioResponseRest response = new UsuarioResponseRest();
		List<UsuarioDto> lista = new ArrayList<>();
		
		try {
			Optional<Usuario> usuarioExist = usuarioRepository.findUsernameByInactivo(username);
			if(usuarioExist.isEmpty()) {
				response.setMetdata("nOk ", "-1", " Usuario no encontrado");
				return new ResponseEntity<UsuarioResponseRest>(response,HttpStatus.NOT_FOUND);
			}
			
			Usuario usuario = usuarioExist.get();
			usuario.setEstado(true);
			
			Usuario usuarioActualizado = usuarioRepository.save(usuario);
			
			UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
	        lista.add(dtoFinal);
	        response.getUsuarioResponse().setUsuario(lista);
	        response.setMetdata("OK", "1", "Usuario Eliminado Logicamente correctamente");
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("Error al actualizar usuario", e);
	        response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}
