package com.proyect.System_userAndLogin.ServicesImpl;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.proyect.System_userAndLogin.Model.Usuario;
import com.proyect.System_userAndLogin.Repository.IUsuarioRepocitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class userNameServicesImpl {

	private final IUsuarioRepocitory usuarioRepository;
	private static final int MAX_LEN =30;
	
	public String generarUsuario(String primerNombre, String apellidoPaterno) {
		String base = buildBase(primerNombre,apellidoPaterno);
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

    private static String buildBase(String nombre, String apellidoPaterno) {
        String a = slug(nombre);
        String b = slug(apellidoPaterno);
        String base = (a.isEmpty() ? "user" : a) + "." + (b.isEmpty() ? "x" : b);
        if (base.length() > MAX_LEN) base = base.substring(0, MAX_LEN);
        return base;
    }

    private static String slug(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");           
        t = t.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]+", "");      
        t = t.replaceAll("\\.{2,}", ".").replaceAll("_{2,}", "_").replaceAll("-{2,}", "-");
        return t;
    }

    private static String appendWithMax(String base, int sufijo, int maxLen) {
        String sfx = (sufijo == 0) ? "" : String.valueOf(sufijo);
        int allow = maxLen - sfx.length();
        String trimmedBase = base.length() > allow ? base.substring(0, allow) : base;
        return trimmedBase + sfx;
    }


}