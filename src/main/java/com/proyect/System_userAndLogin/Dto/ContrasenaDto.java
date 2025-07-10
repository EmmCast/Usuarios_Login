package com.proyect.System_userAndLogin.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContrasenaDto {
    private Long idContrasena;
    private Long idUsuario;
    private String contrasena;
}