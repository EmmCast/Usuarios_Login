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
import org.springframework.transaction.annotation.Transactional;

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


/**
 
 Implementación del servicio de negocio para la gestión de usuarios.
 
 Responsabilidades principales:
 
    Crear usuarios y resolver colisiones de username con reintentos controlados.
    Consultar usuarios por ID o username (solo activos cuando aplica).
    Listar usuarios activos e inactivos.
    Actualización de datos básicos y roles.
    Eliminación lógica (desactivar) y eliminación física (borrado definitivo).
    Reactivación por username.
    Sincronización con servicios relacionados: contraseñas y fotos.
 
  Notas técnicas:
  
    Uso de {@code @Transactional} donde corresponde (escrituras y operaciones compuestas).
    Estrategia de reintentos al generar username cuando hay colisiones (hasta 5 intentos).
    Mapeo entidad ⇄ DTO aislado en un helper privado para mantener consistencia.
  
 
  Las respuestas siguen el patrón ResponseRest para incluir metadatos estándar.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10

 **/
@Service
public class UsuarioServicesImpl implements IUsuarioServices {

    /** Logger de la clase para auditoría y diagnóstico. */
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicesImpl.class);

    /** Repositorio de acceso a datos de usuarios. */
    private final IUsuarioRepocitory usuarioRepository;

    /** Servicio de catálogo de roles. */
    private final IRolServices rolServices;

    /** Servicio de gestión de contraseñas. */
    private final IContrasenaServices contrasenaServices;

    /** Servicio de gestión de fotos de usuario. */
    private final IFotoUsuarioServices fotoUsuarioServices;

    /** Servicio auxiliar para generación de nombres de usuario únicos. */
    private final userNameServicesImpl userNameServ;

    // private final ContrasenaServicesImpl contrasenaServicesImpl;

    /**
     Constructor con inyección de dependencias.
     
     @param usuarioRepository repositorio de usuarios
     @param userNameServ servicio de generación de username
     @param rolServices servicio de roles
     @param contrasenaServices servicio de contraseñas
     @param fotoUsuarioServices servicio de fotos (lazy para evitar ciclos)
     @param contrasenaServicesImpl (no utilizado actualmente)
     
     */
    @Autowired
    public UsuarioServicesImpl(IUsuarioRepocitory usuarioRepository,
                               userNameServicesImpl userNameServ,
                               IRolServices rolServices,
                               IContrasenaServices contrasenaServices,
                               @Lazy IFotoUsuarioServices fotoUsuarioServices,
                               ContrasenaServicesImpl contrasenaServicesImpl) {
        this.usuarioRepository = usuarioRepository;
        this.userNameServ = userNameServ;
        this.rolServices = rolServices;
        this.contrasenaServices = contrasenaServices;
        this.fotoUsuarioServices = fotoUsuarioServices;
        // this.contrasenaServicesImpl = contrasenaServicesImpl;
    }

    /**
     Mapea una entidad {@link Usuario} a su correspondiente {@link UsuarioDto}.
     Incluye extracción de bytes de la foto cuando existe y convierte la colección
     de {@link Rol} en un conjunto de IDs para mantener el desacoplamiento.
     
     @param usuario entidad de usuario origen
     @return DTO con datos proyectados para capa superior
     
     */
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

        Set<Long> rolesIds = (usuario.getRoles() == null)
                ? java.util.Set.of()
                : usuario.getRoles().stream().map(Rol::getIdRol).collect(java.util.stream.Collectors.toSet());
        dto.setRolesIds(rolesIds);

        return dto;
    }

    /**
      Crea un usuario persistiendo en base de datos, generando y asignando
      un {@code username} único a partir del primer nombre y apellido paterno.
     
      Si se detecta colisión de unicidad por nombre de usuario, reintenta hasta 5 veces
      antes de propagar {@link DataIntegrityViolationException}.
     
      @param nuevo entidad usuario a crear (sin ID)
      @return usuario persistido con username asignado
      @throws DataIntegrityViolationException si tras 5 reintentos persiste el conflicto
     */
    @Transactional
    public Usuario crearUsuario(Usuario nuevo) {
        int reintentos = 0;
        while (true) {
            try {
                String username = userNameServ.generarUsuario(nuevo.getPrimerNombre(), nuevo.getApellidoPaterno());
                nuevo.setNombreUsuario(username);
                return usuarioRepository.save(nuevo);
            } catch (DataIntegrityViolationException ex) {
                if (++reintentos > 5) throw ex;
            }
        }
    }

    /**
      Guarda un nuevo usuario a partir de {@link UsuarioDto}, asigna roles,
      genera username único, crea contraseña inicial (usando el username) y,
      opcionalmente, guarda foto de perfil.
     
      En caso de colisión por {@code nombre_usuario}, aplica reintentos controlados.
     
      @param dto datos de entrada
      @return {@link ResponseEntity} con {@link UsuarioResponseRest} y estatus 201 si exitoso
     
     */
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
                    String username = userNameServ.generarUsuario(dto.getPrimerNombre(), dto.getApellidoPaterno());
                    usuario.setNombreUsuario(username);
                    // saveAndFlush para forzar validaciones de unicidad y detectar colisiones inmediatamente
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

            // Contraseña inicial = username (se recomienda forzar cambio al primer login)
            ContrasenaDto contrasena = new ContrasenaDto();
            contrasena.setIdUsuario(usuarioGuardado.getIdUsuario());
            contrasena.setContrasena(usuarioGuardado.getNombreUsuario());
            contrasenaServices.crearContrasena(contrasena);

            // Foto (opcional)
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

    /**
      Busca un usuario ACTIVO por su identificador y devuelve DTO con foto si existe.
     
      @param idUsuario id del usuario
      @return 200 con usuario si existe; 404 si no encontrado; 500 ante error interno
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarPorId(Long idUsuario) {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByIdActivo(idUsuario);

            if (usuarioOpt.isEmpty()) {
                response.setMetdata("nOk", "-1", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Usuario usuario = usuarioOpt.get();
            UsuarioDto dto = mapperUsuarioDto(usuario);

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

    /**
      Busca un usuario ACTIVO por su {@code username} y devuelve DTO con foto si existe.
     
      @param username nombre de usuario (case-insensitive en query nativa)
      @return 200 si existe, 404 si no, 500 en error interno
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarPorUserNameActivo(String username) {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findUsername(username);
            if (usuarioOptional.isEmpty()) {
                response.setMetdata("nOk", "-1", "Usuario No encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Usuario usuario = usuarioOptional.get();
            UsuarioDto dto = mapperUsuarioDto(usuario);

            FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
            if (foto != null) {
                dto.setFotoUsuario(foto.getFoto());
            }

            lista.add(dto);
            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("Ok", "1", "Usuario encontrado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al buscar usuario por username {}", username, e);
            response.setMetdata("nOk", "-1", "Error interno al buscar el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
      Lista todos los usuarios ACTIVOS y adjunta la foto si existe.
     
      @return 200 con lista; 500 en error interno
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarTodosActivos() {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();

        try {
            List<Usuario> usuarios = usuarioRepository.findAllActivos();

            for (Usuario usuario : usuarios) {
                UsuarioDto dto = mapperUsuarioDto(usuario);
                FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
                if (foto != null) {
                    dto.setFotoUsuario(foto.getFoto());
                }
                lista.add(dto);
            }
            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("Ok", "1", "Usuarios Encontrados");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al buscar a todos los usuarios", e);
            response.setMetdata(" nOk", "-1", "Error al buscar los usuarios");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Lista todos los usuarios INACTIVOS y adjunta la foto si existe.
     
      @return 200 con lista; 500 en error interno
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarTodosInactivos() {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();

        try {
            List<Usuario> usuarios = usuarioRepository.findAllInactivos();

            for (Usuario usuario : usuarios) {
                UsuarioDto dto = mapperUsuarioDto(usuario);
                FotoUsuario foto = fotoUsuarioServices.obtenerFotoPorUsuarioId(usuario.getIdUsuario());
                if (foto != null) {
                    dto.setFotoUsuario(foto.getFoto());
                }
                lista.add(dto);
            }
            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("Ok", "1", "Usuarios Encontrados");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al buscar a todos los usuarios inactivos", e);
            response.setMetdata("nOk", "-1", "Error al buscar los usuarios");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Actualiza datos básicos (email, teléfono) y roles del usuario activo.
      Si se envían bytes de foto, actualiza la imagen de perfil.
     
      @param idUsuario id del usuario a actualizar
      @param dto datos a aplicar
      @return 200 si correcto; 404 si no existe; 500 ante error
     */
    @Override
    @Transactional
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

    /**
      Elimina lógicamente (estado = FALSE) a un usuario por ID.
     
      @param idUsuario id del usuario
      @return 200 si correcto; 404 si no existe; 500 en error
     */
    @Override
    @Transactional
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
            response.setMetdata("OK", "1", "Usuario Eliminado Lógicamente correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al eliminar lógicamente usuario", e);
            response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Elimina lógicamente (estado = FALSE) a un usuario por {@code username}.
     
      @param userName nombre de usuario
      @return 200 si correcto; 404 si no existe; 500 en error
     */
    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> eliminarUsuarioLogicoPorUserName(String userName) {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioExist = usuarioRepository.findUsername(userName);
            if (usuarioExist.isEmpty()) {
                response.setMetdata("nOk ", "-1", " Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Usuario usuario = usuarioExist.get();
            usuario.setEstado(false);

            Usuario usuarioActualizado = usuarioRepository.save(usuario);

            UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
            lista.add(dtoFinal);
            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("OK", "1", "Usuario Eliminado Lógicamente correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al eliminar lógicamente por username", e);
            response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Elimina físicamente (DELETE) a un usuario por ID.
     
      @param idUsuario id del usuario
      @return 200 si correcto; 404 si no existe; 500 en error
     */
    @Override
    @Transactional
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

            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("OK", "1", "Usuario Eliminado Permanentemente correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al eliminar físicamente usuario", e);
            response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Elimina físicamente (DELETE) a un usuario por {@code username}.
     
      @param userName nombre de usuario
      @return 200 si correcto; 404 si no existe; 500 en error
     */
    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> eliminarUsuarioFisicoPorUserName(String userName) {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioExist = usuarioRepository.findUsername(userName);
            if (usuarioExist.isEmpty()) {
                response.setMetdata("nOk ", "-1", " Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Usuario usuario = usuarioExist.get();
            usuarioRepository.deleteById(usuario.getIdUsuario());

            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("OK", "1", "Usuario Eliminado Permanentemente correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al eliminar físicamente por username", e);
            response.setMetdata("nOk", "-1", "No se pudo Eliminar el usuario");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
      Reactiva un usuario (estado = TRUE) por {@code username}.
     
      @param username nombre de usuario inactivo
      @return 200 si reactivado; 404 si no existe; 500 en error
     */
    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> reactivarUsuariosPorUsername(String username) {
        UsuarioResponseRest response = new UsuarioResponseRest();
        List<UsuarioDto> lista = new ArrayList<>();

        try {
            Optional<Usuario> usuarioExist = usuarioRepository.findUsernameByInactivo(username);
            if (usuarioExist.isEmpty()) {
                response.setMetdata("nOk ", "-1", " Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Usuario usuario = usuarioExist.get();
            usuario.setEstado(true);

            Usuario usuarioActualizado = usuarioRepository.save(usuario);

            UsuarioDto dtoFinal = mapperUsuarioDto(usuarioActualizado);
            lista.add(dtoFinal);
            response.getUsuarioResponse().setUsuario(lista);
            response.setMetdata("OK", "1", "Usuario reactivado correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al reactivar usuario por username", e);
            response.setMetdata("nOk", "-1", "No se pudo reactivar el usuario");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
