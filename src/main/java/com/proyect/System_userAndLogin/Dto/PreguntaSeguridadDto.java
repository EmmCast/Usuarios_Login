package com.proyect.System_userAndLogin.Dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaSeguridadDto  implements Serializable{
	
	private static final long serialVersionUID = 8564890825891991218L;

	    private String pregunta;
	    private String respuesta;
	    private Long usuarioId;

}
