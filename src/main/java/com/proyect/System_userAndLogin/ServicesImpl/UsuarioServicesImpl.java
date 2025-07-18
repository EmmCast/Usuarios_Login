package com.proyect.System_userAndLogin.ServicesImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.UsuarioDto;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;
import com.proyect.System_userAndLogin.Response.ResponseUsuario.UsuarioResponseRest;
import com.proyect.System_userAndLogin.Services.IContrasenaServices;
import com.proyect.System_userAndLogin.Services.IFotoUsuarioServices;
import com.proyect.System_userAndLogin.Services.IRolServices;
import com.proyect.System_userAndLogin.Services.IUsuarioServices;

@Service
public class UsuarioServicesImpl implements IUsuarioServices{

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicesImpl.class);
	
	@Autowired
	private IUsuarioRepocitory usuarioRepository;
	
	@Autowired
	private IRolServices rolServices;
	
	@Autowired
	private IContrasenaServices contrasenaServices;
	
	@Autowired
	private IFotoUsuarioServices fotoUsuarioServices;
	
	public UsuarioServicesImpl (IUsuarioRepocitory usuarioRepository,IRolServices rolServices,
			IContrasenaServices contrasenaServices,IFotoUsuarioServices fotoUsuarioServices) {
		this.usuarioRepository = usuarioRepository;
		this.rolServices = rolServices;
		this.contrasenaServices = contrasenaServices;
		this.fotoUsuarioServices = fotoUsuarioServices;
	}

	@Override
	public UsuarioResponseRest guardarUsuario(UsuarioDto dto) {
		return null;
	}

	@Override
	public UsuarioResponseRest buscarPorId(Long idUsuario) {
		return null;
	}

	@Override
	public UsuarioResponseRest buscarTodos() {
		return null;
	}

	@Override
	public UsuarioResponseRest eliminarUsuario(Long idUsuario) {
		return null;
	}

	@Override
	public UsuarioResponseRest actualizarUsuario(Long idUsuario, UsuarioDto dto) {
		return null;
	}
	
	
}
