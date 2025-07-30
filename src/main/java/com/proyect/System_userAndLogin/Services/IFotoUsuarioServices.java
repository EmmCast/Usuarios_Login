package com.proyect.System_userAndLogin.Services;

import com.proyect.System_userAndLogin.Model.FotoUsuario;

public interface IFotoUsuarioServices {

    FotoUsuario guardarFoto(Long idUsuario, byte[] imagen);
    FotoUsuario actualizarFoto(Long idUsuario, byte[] imagen);
    FotoUsuario obtenerFotoPorUsuarioId(Long idUsuario);
    void eliminarFoto(Long idUsuario);
    
}
