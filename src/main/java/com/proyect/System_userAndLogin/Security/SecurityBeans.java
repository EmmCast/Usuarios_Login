package com.proyect.System_userAndLogin.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 Configuración de beans relacionados con seguridad.
 
 Expone un {@link PasswordEncoder} basado en BCrypt con un costo de 12,
 recomendable como punto de equilibrio entre seguridad y rendimiento para
 la mayoría de aplicaciones web.
 
   Notas
 	Incrementar el costo (work factor) aumenta el tiempo de cómputo de verificación.
 	Para cargas muy altas, medir el impacto de subir o bajar el costo.
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 */
@Configuration
public class SecurityBeans {

    /**
     Crea el codificador de contraseñas BCrypt con costo 12.
     
     @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(12);
    }
}