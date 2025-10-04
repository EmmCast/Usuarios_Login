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


/**
  Implementación del servicio de negocio para el catálogo de roles.
 
  Responsabilidades:
  
    Listar todos los roles existentes en el sistema.
    Buscar un rol por su identificador y devolverlo en forma de entidad.
  
 
  Notas técnicas:
  
    Utiliza inyección por constructor (recomendada para testabilidad).
    Registra eventos con {@link Logger} para facilitar auditoría y diagnóstico.
  
 
  Conversión a DTO:
  
    El método {@link #listarRoles()} transforma entidades {@link Rol} a {@link RolDto}
        para mantener el desacoplamiento con la capa de presentación.
  
 
  Errores:
  
    {@link #buscarPorId(Long)} lanza {@link EntityNotFoundException} si no existe el rol.
  
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/
@Service
public class RolServicesImpl implements IRolServices {

    /** Logger de la clase para trazas informativas y de error. */
    private static final Logger logger = LoggerFactory.getLogger(RolServicesImpl.class);

    /** Repositorio de acceso a datos de roles. */
    private final IRolRepository rolRepository;

    /**
      Crea una instancia del servicio de roles.
     
      @param rolRepository repositorio para operaciones CRUD sobre {@link Rol}
     */
    public RolServicesImpl(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
      Lista todos los roles disponibles y los proyecta a {@link RolDto}.
     
      @return lista de roles en formato DTO
     */
    @Override
    public List<RolDto> listarRoles() {
        logger.info("Listando todos los roles");
        List<Rol> roles = rolRepository.findAll();
        return roles.stream()
                .map(rol -> new RolDto(rol.getIdRol(), rol.getRol()))
                .collect(Collectors.toList());
    }

    /**
      Busca un rol por su identificador.
     
      @param id identificador del rol
      @return la entidad {@link Rol} encontrada
      @throws EntityNotFoundException si no existe un rol con el id proporcionado
     */
    @Override
    public Rol buscarPorId(Long id) {
        logger.info("Buscando rol por id: {}", id);
        return rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));
    }
}