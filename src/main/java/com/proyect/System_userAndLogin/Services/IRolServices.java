package com.proyect.System_userAndLogin.Services;

import java.util.List;

import com.proyect.System_userAndLogin.Dto.RolDto;
import com.proyect.System_userAndLogin.Model.Rol;

public interface IRolServices {
	List<RolDto> listarRoles();
    Rol buscarPorId(Long idRol);
}
