package com.proyect.System_userAndLogin.Response;

import java.util.ArrayList;
import java.util.HashMap;

/**
 
 Clase base para las respuestas REST del sistema.
  
  Proporciona una estructura de metadatos que acompaña a cada respuesta
  de los servicios. Los metadatos incluyen:
  
    type → tipo de respuesta (ej. OK, ERROR, WARNING)
    code → código numérico o de estado
    date → mensaje asociado o marca de tiempo
  
  
  Se utiliza como clase padre para todas las respuestas especializadas
  (ej. {@link UsuarioResponseRest}, {@link ContrasenaResponseRest}).
  
  @author Emmanuel
  @version 1.5
  @since 2025-10
 
 **/

public class ResponseRest {

    /** Lista de mapas con metadatos que describen la respuesta. */
    private ArrayList<HashMap<String, String>> metdata = new ArrayList<>();

    public ArrayList<HashMap<String, String>> getMetdata() {
        return metdata;
    }

    /**
     Agrega un metadato a la respuesta.
     
     @param type tipo de la respuesta (ej. "OK", "ERROR")
     @param code código asociado a la respuesta
     @param date mensaje o fecha de la respuesta
     
     **/
    public void setMetdata(String type, String code, String date) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("code", code);
        map.put("date", date);
        metdata.add(map);
    }
}