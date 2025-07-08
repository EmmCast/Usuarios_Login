package com.proyect.System_userAndLogin.Dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RolDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 private Long idRol;
	 private String rol;
	 
}