package com.proyect.System_userAndLogin.ServicesImpl;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyect.System_userAndLogin.Model.Contrasena;
import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IContrasenaReposiroty;
import com.proyect.System_userAndLogin.Repository.IRolRepository;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;

import java.util.*;

/**
 Servicio de autenticación.
 
 Resuelve el flujo de login:
 
 Busca al usuario por login (username o email).
 	Obtiene el hash de contraseña y valida usando {@link PasswordEncoder}.
    Recupera los roles del usuario.
    Genera un JWT con {@link JwtService} y retorna el payload de autenticación.
 
 	Seguridad
 		Nunca registrar la contraseña en claro en logs.
 		El hash debe almacenarse sólo en BD; la app no debe revertirlo.
 		Este servicio no maneja expiración/refresh de tokens; sólo emite el access token.
 
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 */
@Service
public class AuthService {

    private final IUsuarioRepocitory usuarioRepo;
    private final IContrasenaReposiroty contrasenaRepo;
    private final IRolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     Crea el servicio de autenticación con sus dependencias.
     
     @param contrasenaRepo repositorio de contraseñas (hash por usuario)
     @param rolRepo repositorio de roles
     @param usuarioRepo repositorio de usuarios
     @param passwordEncoder encoder para verificar BCrypt
     @param jwtService servicio de emisión de JWT
     */
    public AuthService(IContrasenaReposiroty contrasenaRepo,
                       IRolRepository rolRepo,
                       IUsuarioRepocitory usuarioRepo,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepo = usuarioRepo;
        this.contrasenaRepo = contrasenaRepo;
        this.rolRepo = rolRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     Realiza el inicio de sesión y devuelve la información de autenticación.
     
      Si las credenciales son válidas, retorna un mapa con:
      
      	tokenType: "Bearer"
     	accessToken: JWT firmado
     	userId, username, email, roles
          
     @param login valor de login (username o email)
     @param password contraseña en claro a validar
     @return mapa con el token y datos básicos del usuario
     @throws IllegalArgumentException si el usuario no existe, no tiene contraseña o las credenciales son inválidas
     */
    @Transactional(readOnly = true)
    public Map<String, Object> login(String login, String password) {
        Usuario u = usuarioRepo.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuario/Email no encontrado"));

        // Ajusta el método según tu repositorio/entidad (por ejemplo: findByUsuarioIdUsuario)
        Contrasena c = contrasenaRepo.findByUsuarioId(u.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario sin contraseña"));

        // Ajusta el getter según tu entidad (por ejemplo: getContrasenaHash)
        if (!passwordEncoder.matches(password, c.getContrasena())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        List<String> roles = rolRepo.findRoleNamesByUsuarioId(u.getIdUsuario());

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", u.getIdUsuario());
        claims.put("roles", roles);
        claims.put("email", u.getEmail());

        // Ajusta el getter del username si tu entidad usa otro nombre (p. ej. getUsername)
        String token = jwtService.generateToken(u.getNombreUsuario(), claims);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("tokenType", "Bearer");
        resp.put("accessToken", token);
        resp.put("userId", u.getIdUsuario());
        resp.put("username", u.getNombreUsuario());
        resp.put("email", u.getEmail());
        resp.put("roles", roles);
        return resp;
    }
}