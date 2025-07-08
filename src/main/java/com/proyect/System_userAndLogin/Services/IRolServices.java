package com.proyect.System_userAndLogin.Services;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.RolDto;

public interface IRolServices {

	List<RolDto> listarRoles();
    RolDto buscarPorId(Long idRol);
}
