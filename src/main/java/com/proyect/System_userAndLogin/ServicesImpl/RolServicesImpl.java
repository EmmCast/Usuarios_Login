package com.proyect.System_userAndLogin.ServicesImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Dto.RolDto;
import com.proyect.System_userAndLogin.Model.Rol;
import com.proyect.System_userAndLogin.Repository.IRolRepository;
import com.proyect.System_userAndLogin.Services.IRolServices;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RolServicesImpl implements IRolServices{

	private static final Logger logger = LoggerFactory.getLogger(RolServicesImpl.class);
	
	private IRolRepository rolRepository;
	
	public RolServicesImpl(IRolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}
	
	@Override
	public List<RolDto> listarRoles() {
        logger.info("Listando todos los roles");
        List<Rol> roles = rolRepository.findAll();
        return roles.stream()
        		.map(rol -> new RolDto(rol.getIdRol(), rol.getRol()))
        		.collect(Collectors.toList());
    }


	@Override
	public RolDto buscarPorId(Long id) {
	    logger.info("Buscando rol por id: {}", id);

	    Rol rol = rolRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));

	    return new RolDto(rol.getIdRol(), rol.getRol());
	}
}
