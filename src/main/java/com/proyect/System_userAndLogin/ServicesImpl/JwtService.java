package com.proyect.System_userAndLogin.ServicesImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;


/**
 Servicio para emisión de tokens JWT (HS256).
 
 Construye la llave HMAC a partir de una cadena Base64 configurada en
 app.jwt.secret-base64 y permite generar tokens con claims adicionales.
 
 
 	Requisitos de configuración

		app.jwt.issuer: emisor del token.
 		app.jwt.expiration-ms: tiempo de expiración en milisegundos.
 		app.jwt.secret-base64: clave HMAC en Base64 (>= 256 bits).
 
 
 	Seguridad
 
 		No exponer ni versionar la clave; usar variables de entorno o vault.
 		La longitud mínima recomendada es 32 bytes (256 bits) antes de Base64.
 
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 */
@Service
public class JwtService {

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    @Value("${app.jwt.secret-base64}")
    private String secretBase64;

    private SecretKey key;

    /**
     Inicializa la llave HMAC a partir del secreto en Base64.
     
     Se ejecuta una vez al iniciar el contexto de Spring. Si el secreto no está
     definido o es inválido, la aplicación debería fallar rápido para evitar
     emitir tokens inseguros.
     
     @throws IllegalStateException si la clave no está configurada o es demasiado corta
     */
    @PostConstruct
    void init() {
        Objects.requireNonNull(secretBase64, "Falta app.jwt.secret-base64");
        byte[] secret = Decoders.BASE64.decode(secretBase64.trim());
        if (secret.length < 32) {
            throw new IllegalStateException("app.jwt.secret-base64 es muy corto; mínimo 256 bits");
        }
        this.key = Keys.hmacShaKeyFor(secret);
    }

    /**
     Genera un token JWT firmado con HS256.
     
     @param subject     sujeto del token (típicamente el username)
     @param extraClaims mapa de claims adicionales (por ejemplo: uid, roles, email)
     @return cadena JWT compacta
     @throws IllegalStateException si la llave no fue inicializada
     */
    public String generateToken(String subject, Map<String, Object> extraClaims) {
        if (key == null) {
            throw new IllegalStateException("JWT key no inicializada");
        }
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .addClaims(extraClaims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}