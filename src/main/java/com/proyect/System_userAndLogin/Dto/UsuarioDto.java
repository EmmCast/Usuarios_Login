package com.proyect.System_userAndLogin.Dto;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable{
	
	private static final long serialVersionUID = -7970922455652136120L;

	private Long idUsuario;
	
	@NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 30 caracteres")
	private String primerNombre;
	
	private String segundoNombre;

	@NotBlank(message = "El Apellido Materno es obligatorio")
    @Size(max = 30, message = "El Apellido Materno no puede exceder los 30 caracteres")
	private String apellidoPaterno;
	
	@NotBlank(message = "El apellido Materno es obligatorio")
    @Size(max = 30, message = "El apellido Materno no puede exceder los 30 caracteres")
	private String apellidoMaterno;
	
	@NotBlank(message = "El email es obligatorio")
    @Size(max = 50, message = "El email no puede exceder los 50 caracteres")
	private String email;
	
	@NotBlank(message = "El Telefono es obligatorio")
    @Size(max = 10, message = "El Telefono no puede exceder los 10 caracteres")
	private String telefono;
	
	private String nombreUsuario;
	
	private byte[] fotoUsuario;   
	
	private Date fechaIngreso;
	
	@NotBlank(message = "El Rol es obligatorio")
	private Set<Long> rolesIds = new HashSet<>();
	
}
