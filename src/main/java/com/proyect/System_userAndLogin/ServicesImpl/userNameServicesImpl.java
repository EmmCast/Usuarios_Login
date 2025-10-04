package com.proyect.System_userAndLogin.ServicesImpl;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;

import lombok.RequiredArgsConstructor;


/**
  Servicio auxiliar para la generación de nombres de usuario únicos.
 
  Reglas principales:
  
    La base del username se construye como slug(primerNombre) + "." + slug(apellidoPaterno).
    Se normalizan acentos y caracteres especiales a ASCII, se convierte a minúsculas y
        solo se permiten [a-z0-9._-].
    Longitud máxima configurable (ver {@link #MAX_LEN}).
    Si el username base ya existe, se añade un sufijo numérico incremental (1, 2, 3, ...).
    Se evita colisiones consultando la BD (prefetch para sufijos usados y verificación final con existsBy...).>
  
 
  Notas de implementación:
  
    Se realiza un prefiltrado de usernames que comparten la base para detectar sufijos utilizados.
    Se reintenta hasta 20 veces en la verificación final antes de abortar con {@link IllegalStateException}.
  
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
@Service
@RequiredArgsConstructor
public class userNameServicesImpl {

    /** Repositorio de usuarios para verificar existencia y listar prefijos existentes. */
    private final IUsuarioRepocitory usuarioRepository;

    /** Longitud máxima permitida para el username final. */
    private static final int MAX_LEN = 30;

    /**
      Genera un nombre de usuario único a partir de un nombre y apellido paterno.
     
      El proceso consiste en:
      
        Construir la base con {@link #buildBase(String, String)}.
        Consultar usernames existentes que empiecen por esa base.
        Detectar sufijos numéricos ya usados y elegir el siguiente disponible.
        Verificar en BD con existsByNombreUsuarioIgnoreCase y, si colisiona, incrementar sufijo.
      
     
      @param primerNombre primer nombre del usuario (puede venir con acentos/espacios)
      @param apellidoPaterno apellido paterno del usuario
      @return username único respetando las reglas de normalización y longitud
      @throws IllegalStateException si tras 20 intentos no es posible generar un username único
     */
    public String generarUsuario(String primerNombre, String apellidoPaterno) {
        String base = buildBase(primerNombre, apellidoPaterno);
        List<String> existentes = usuarioRepository.findUsernames(base);

        Set<Integer> usados = new HashSet<>();
        Pattern p = Pattern.compile("^" + Pattern.quote(base) + "(\\d+)?$", Pattern.CASE_INSENSITIVE);

        for (String s : existentes) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                if (m.group(1) == null) {
                    usados.add(0);
                } else {
                    try {
                        usados.add(Integer.parseInt(m.group(1)));
                    } catch (NumberFormatException ignore) {}
                }
            }
        }

        int sufijo = 0;
        while (usados.contains(sufijo)) sufijo++;

        String candidate = appendWithMax(base, sufijo, MAX_LEN);

        int intentos = 0;
        while (usuarioRepository.existsByNombreUsuarioIgnoreCase(candidate)) {
            sufijo++;
            candidate = appendWithMax(base, sufijo, MAX_LEN);
            if (++intentos > 20) {
                throw new IllegalStateException("No fue posible generar un username único");
            }
        }
        return candidate;
    }

    /**
      Construye la cadena base del username uniendo nombre y apellido con un punto,
      tras normalizar cada parte con {@link #slug(String)} y recortar a la longitud máxima.
     
      Si el nombre está vacío, usa user; si el apellido está vacío, usa x.
     
      @param nombre primer nombre
      @param apellidoPaterno apellido paterno
      @return base normalizada y acotada por longitud, p. ej. <juan.perez
     */
    private static String buildBase(String nombre, String apellidoPaterno) {
        String a = slug(nombre);
        String b = slug(apellidoPaterno);
        String base = (a.isEmpty() ? "user" : a) + "." + (b.isEmpty() ? "x" : b);
        if (base.length() > MAX_LEN) base = base.substring(0, MAX_LEN);
        return base;
    }

    /**
      Normaliza una cadena para uso en username:
      
        Elimina tildes/diacríticos (NFD → sin marcas).
        Convierte a minúsculas Locale.ROOT
        Filtra cualquier carácter fuera de [a-z0-9._-]
        Colapsa repeticiones de .,  y .
      
     
      @param s cadena de entrada (puede ser null)
      @return cadena normalizada (posiblemente vacía si no hay caracteres válidos)
     */
    private static String slug(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");           // quita marcas de acento
        t = t.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]+", "");     // permite solo [a-z0-9._-]
        t = t.replaceAll("\\.{2,}", ".")
             .replaceAll("_{2,}", "_")
             .replaceAll("-{2,}", "-");               // colapsa repeticiones
        return t;
    }

    /**
      Agrega un sufijo numérico a la base respetando la longitud máxima permitida.
      Si el sufijo es 0, no se agrega nada.
     
      @param base base del username
      @param sufijo valor numérico a anexar (0 = sin sufijo)
      @param maxLen longitud máxima del resultado
      @return username candidato recortado si es necesario para no exceder {@code maxLen}
     */
    private static String appendWithMax(String base, int sufijo, int maxLen) {
        String sfx = (sufijo == 0) ? "" : String.valueOf(sufijo);
        int allow = maxLen - sfx.length();
        String trimmedBase = base.length() > allow ? base.substring(0, allow) : base;
        return trimmedBase + sfx;
    }
}