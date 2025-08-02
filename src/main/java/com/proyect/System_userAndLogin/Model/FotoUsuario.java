package com.proyect.System_userAndLogin.Model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FotosDUsuarios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FotoUsuario implements Serializable{
	
	private static final long serialVersionUID = 8564890825891991218L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto_usuario")
    private Long idFotoUsuario;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto_usuario", nullable = false)
    private byte[] fotoUsuario;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}

