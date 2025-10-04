package com.proyect.System_userAndLogin.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
  Configuración principal de Spring Security para la aplicación.
 
  Estado actual:
  
    CSRF deshabilitado.
    Todas las solicitudes están permitidas sin autenticación.
  
 
  Uso recomendado: esta configuración es útil en entornos de
  desarrollo o para pruebas iniciales. En producción se recomienda:
  
    Habilitar CSRF en endpoints que lo requieran (formularios, sesiones).
    Restringir accesos por rutas/roles y agregar autenticación/autoridades.
    Configurar headers de seguridad (CSP, HSTS, X-Content-Type-Options, etc.).
 
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
      Define la cadena de filtros de seguridad de Spring Security.
    
      Implementación actual:
      
        csrf.disable(): deshabilita la protección CSRF.
        authorizeHttpRequests(...).anyRequest().permitAll():
            permite todas las solicitudes sin autenticación.
     
      @param http instancia de {@link HttpSecurity} para construir la configuración
      @return la {@link SecurityFilterChain} resultante
      @throws Exception si ocurre algún error al construir la cadena de filtros
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}