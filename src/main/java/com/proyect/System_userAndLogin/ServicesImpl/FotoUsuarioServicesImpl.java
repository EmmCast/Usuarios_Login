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

@Service
public class FotoUsuarioServicesImpl implements IFotoUsuarioServices{
	
	private static final Logger logger = LoggerFactory.getLogger(FotoUsuarioServicesImpl.class);
	
	private final IFotoUsuarioRepository fotoUsuarioRepository;
	
	private final IUsuarioServices usuarioServices;
/*	
	@Autowired
	private FotoUsuarioServicesImpl(@Lazy IFotoUsuarioRepository fotoUsuarioRepository,
			IUsuarioServices usuarioServices) {
		this.fotoUsuarioRepository = fotoUsuarioRepository;
		this.usuarioServices = usuarioServices;
	}
*/
	
	@Autowired
	public FotoUsuarioServicesImpl(@Lazy IFotoUsuarioRepository fotoUsuarioRepository,
	                               IUsuarioServices usuarioServices) {
	    this.fotoUsuarioRepository = fotoUsuarioRepository;
	    this.usuarioServices = usuarioServices;
	}
	
	 public FotoUsuario guardarFoto(Long idUsuario, byte[] imagen) {
			    try {
			        ResponseEntity<UsuarioResponseRest> response = usuarioServices.buscarPorId(idUsuario);
			        if (response.getStatusCode() != HttpStatus.OK || response.getBody().getUsuarioResponse().getUsuario().isEmpty()) {
			            logger.warn("Usuario con ID {} no encontrado para guardar foto", idUsuario);
			            return null;
			        }

			        FotoUsuario nuevaFoto = new FotoUsuario();
			        Usuario usuario = new Usuario();
			        usuario.setIdUsuario(idUsuario);
			        nuevaFoto.setUsuario(usuario);
			        nuevaFoto.setFoto(imagen);
			        nuevaFoto.getCreado();

			        return fotoUsuarioRepository.save(nuevaFoto);
			    } catch (Exception e) {
			        logger.error("Error al guardar la foto del usuario con ID " + idUsuario, e);
			        return null;
			    }
			}

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

	    @Override
	    public FotoUsuario obtenerFotoPorUsuarioId(Long idUsuario) {
	        try {
	            return fotoUsuarioRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);
	        } catch (Exception e) {
	            logger.error("Error al obtener la foto del usuario con ID " + idUsuario, e);
	            return null;
	        }
	    }

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
