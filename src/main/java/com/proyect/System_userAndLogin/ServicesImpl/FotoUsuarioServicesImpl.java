package com.proyect.System_userAndLogin.ServicesImpl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Model.FotoUsuario;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IFotoUsuarioRepository;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IFotoUsuarioServices;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;

import java.util.Optional;

import org.slf4j.Logger;


/**
  Implementación del servicio para la gestión de fotos de perfil de usuarios.
 
  Responsabilidades:
  
    Guardar una nueva foto para un usuario existente.
    Actualizar la foto de un usuario (o crearla si no existe).
    Obtener la foto por id de usuario.
    Eliminar la foto asociada a un usuario.
  
 
  Notas técnicas:
  
    Se apoya en {@link IUsuarioServices#buscarPorId(Long)} para verificar existencia del usuario
        antes de guardar una nueva foto.
    La inyección de {@code IFotoUsuarioRepository} es {@code @Lazy} para evitar ciclos, si los hubiera.
    Ante errores se registran trazas y el método retorna {@code null} o no lanza excepción,
        manteniendo el contrato actual.
  
 
  Las implementaciones que consuman este servicio deben manejar los posibles {@code null}
  de retorno y estados HTTP provenientes de {@code IUsuarioServices}.
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
@Service
public class FotoUsuarioServicesImpl implements IFotoUsuarioServices {

    /** Logger de la clase para auditoría y diagnóstico. */
    private static final Logger logger = LoggerFactory.getLogger(FotoUsuarioServicesImpl.class);

    /** Repositorio de persistencia de fotos de usuario. */
    private final IFotoUsuarioRepository fotoUsuarioRepository;

    /** Servicio de usuarios utilizado para validar existencia antes de crear foto. */
    private final IUsuarioServices usuarioServices;

    /*
    @Autowired
    private FotoUsuarioServicesImpl(@Lazy IFotoUsuarioRepository fotoUsuarioRepository,
            IUsuarioServices usuarioServices) {
        this.fotoUsuarioRepository = fotoUsuarioRepository;
        this.usuarioServices = usuarioServices;
    }
    */

    /**
      Constructor con inyección de dependencias.
     
      @param fotoUsuarioRepository repositorio de fotos
      @param usuarioServices servicio de usuarios para validaciones previas
     */
    @Autowired
    public FotoUsuarioServicesImpl(@Lazy IFotoUsuarioRepository fotoUsuarioRepository,
                                   IUsuarioServices usuarioServices) {
        this.fotoUsuarioRepository = fotoUsuarioRepository;
        this.usuarioServices = usuarioServices;
    }

    /**
      Guarda una nueva foto para el usuario indicado, si el usuario existe.
     
      Verifica la existencia del usuario consultando {@link IUsuarioServices#buscarPorId(Long)}.
      Si no existe, retorna {@code null}. Si existe, crea un registro {@link FotoUsuario}
      asociado y persiste los bytes recibidos.
     
      @param idUsuario id del usuario
      @param imagen arreglo de bytes de la imagen a guardar
      @return la entidad {@link FotoUsuario} persistida, o {@code null} si el usuario no existe o ante error
     */
    public FotoUsuario guardarFoto(Long idUsuario, byte[] imagen) {
        try {
            ResponseEntity<UsuarioResponseRest> response = usuarioServices.buscarPorId(idUsuario);
            if (response.getStatusCode() != HttpStatus.OK
                    || response.getBody().getUsuarioResponse().getUsuario().isEmpty()) {
                logger.warn("Usuario con ID {} no encontrado para guardar foto", idUsuario);
                return null;
            }

            FotoUsuario nuevaFoto = new FotoUsuario();
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            nuevaFoto.setUsuario(usuario);
            nuevaFoto.setFoto(imagen);
            nuevaFoto.getCreado(); // acceso no-op a la fecha de creado (se mantiene la lógica original)

            return fotoUsuarioRepository.save(nuevaFoto);
        } catch (Exception e) {
            logger.error("Error al guardar la foto del usuario con ID " + idUsuario, e);
            return null;
        }
    }

    /**
      Actualiza la foto del usuario si existe; en caso contrario, delega en {@link #guardarFoto(Long, byte[])}.
     
      @param idUsuario id del usuario
      @param imagen bytes de la nueva imagen
      @return entidad {@link FotoUsuario} actualizada o creada; {@code null} ante error
     */
    @Override
    public FotoUsuario actualizarFoto(Long idUsuario, byte[] imagen) {
        try {
            Optional<FotoUsuario> fotoExistente = fotoUsuarioRepository.findByUsuario_IdUsuario(idUsuario);

            if (fotoExistente.isPresent()) {
                FotoUsuario foto = fotoExistente.get();
                foto.setFoto(imagen);
                return fotoUsuarioRepository.save(foto);
            } else {
                return guardarFoto(idUsuario, imagen);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar la foto para el usuario " + idUsuario, e);
            return null;
        }
    }

    /**
      Obtiene la foto asociada a un usuario por su id.
     
      @param idUsuario id del usuario
      @return {@link FotoUsuario} si existe, o {@code null} si no hay foto o ante error
     */
    @Override
    public FotoUsuario obtenerFotoPorUsuarioId(Long idUsuario) {
        try {
            return fotoUsuarioRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);
        } catch (Exception e) {
            logger.error("Error al obtener la foto del usuario con ID " + idUsuario, e);
            return null;
        }
    }

    /**
      Elimina la foto asociada al usuario si existe.
     
      @param idUsuario id del usuario
     */
    @Override
    public void eliminarFoto(Long idUsuario) {
        try {
            Optional<FotoUsuario> fotoOpt = fotoUsuarioRepository.findByUsuario_IdUsuario(idUsuario);
            fotoOpt.ifPresent(fotoUsuarioRepository::delete);
        } catch (Exception e) {
            logger.error("Error al eliminar la foto del usuario con ID " + idUsuario, e);
        }
    }
}
