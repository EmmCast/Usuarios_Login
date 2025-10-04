package com.proyect.System_userAndLogin.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
  Utilidades comunes para:
  
    Hashing y verificación de contraseñas con BCrypt.
    Compresión y descompresión de bytes (por ejemplo, imágenes) usando ZLIB.
  
 
  Seguridad: Las contraseñas jamás se almacenan en claro; se hashean con BCrypt.
  Evita registrar (loggear) contraseñas o hashes. Valida nulos antes de invocar los métodos.
 
  Compresión: Los métodos de (des)compresión utilizan ZLIB
  ({@link Deflater}/{@link Inflater}), no GZIP. Se recomienda controlar tamaños de entrada/salida
  para evitar consumos excesivos de memoria.
 
  Thread-safety: {@link BCryptPasswordEncoder} se usa como instancia estática y es segura
  para uso concurrente en escenarios típicos.
 
  @author Emmanuel
  @version 1.5
  @since 2025-10
 */
public class Util {

    /** Codificador BCrypt para hashear y verificar contraseñas. */
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
      Genera el <em>hash</em> BCrypt de una contraseña en texto plano.
     
      @param plainPassword contraseña en texto claro (no nula)
      @return cadena con el hash resultante (incluye salt y factor de costo)
      @throws NullPointerException si {@code plainPassword} es {@code null}
     
      @implSpec usa {@link BCryptPasswordEncoder#encode(CharSequence)}.
      @apiNote El costo por defecto de {@code BCryptPasswordEncoder} suele ser 10;
               ajústalo si necesitas mayor entropía/costo computacional.
     */
    public static String encriptarTexto(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
      Verifica si la contraseña en texto plano coincide con el hash almacenado.
     
      @param plainPassword contraseña en texto claro a validar (no nula)
      @param hashedPassword hash BCrypt previamente almacenado (no nulo)
      @return {@code true} si coincide; {@code false} en caso contrario
      @throws NullPointerException si alguno de los parámetros es {@code null}
     
      @implSpec usa {@link BCryptPasswordEncoder#matches(CharSequence, String)}.
     */
    public static boolean verificarTexto(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    /**
      Comprime un arreglo de bytes utilizando el algoritmo ZLIB.
     
      @param data bytes de entrada a comprimir (no nulos)
      @return bytes comprimidos
      @throws NullPointerException si {@code data} es {@code null}
     
      @implSpec Usa {@link Deflater} con configuración por defecto y un búfer de 1024 bytes.
      @apiNote Este método cierra internamente el {@link ByteArrayOutputStream}. Evita imprimir en consola
               en producción; la línea de System.out.println existe solo para diagnóstico.
     */
    public static byte[] compressZLib(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            // silencioso por compatibilidad con la implementación existente
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    /**
      Descomprime un arreglo de bytes previamente comprimido con ZLIB.
     
      @param data bytes comprimidos (no nulos)
      @return bytes descomprimidos
      @throws NullPointerException si {@code data} es {@code null}
     
      @implSpec Usa {@link Inflater} con configuración por defecto y un búfer de 1024 bytes.
      @apiNote Se capturan y silencian {@link IOException} y {@link DataFormatException} para
               mantener el contrato actual; considera manejar estos casos y loggear en tu aplicación.
     */
    public static byte[] decompressZLib(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
            // silencioso por compatibilidad con la implementación existente
        } catch (DataFormatException e) {
            // silencioso por compatibilidad con la implementación existente
        }
        return outputStream.toByteArray();
    }
}