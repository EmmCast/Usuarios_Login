package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuarios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario  implements Serializable{

	private static final long serialVersionUID = -3774119125088387327L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;

	@Column(name = "nombre_usuario")
	private String nombreUsuario;
	
	@Column(name = "primer_nombre")
	private String primerNombre;
	
	@Column(name = "segundo_nombre")
	private String segundoNombre;
	
	@Column(name = "apellido_paterno")
	private String apellidoPaterno;
	
	@Column(name = "apellido_materno")
	private String apellidoMaterno;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "fecha_ingreso")
	private Date fefhaIngreso;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "usuario_rol",
	    joinColumns = @JoinColumn(name = "usuario_id"),
	    inverseJoinColumns = @JoinColumn(name = "rol_id")
	)
	private Set<Rol> roles = new HashSet<>();
	
	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private FotoUsuario fotoUsuarioId;
	
	@Column(name = "estado")
	private Boolean estado;

}
