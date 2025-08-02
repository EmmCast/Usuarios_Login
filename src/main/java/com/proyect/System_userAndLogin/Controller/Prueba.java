package com.proyect.System_userAndLogin.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.RolDto;
import com.proyect.System_userAndLogin.Model.Rol;
import com.proyect.System_userAndLogin.Services.IRolServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/prueba")
public class Prueba {
	
	@Autowired
	private IRolServices rolServices;
	
	@GetMapping("/listarRol")
	public List<RolDto> listarRol(){
		return rolServices.listarRoles();
	}
	
	@GetMapping("/roles/{id}")
	public ResponseEntity<?> buscarRolPorId(@PathVariable Long id) {
	    try {
	        Rol rolDto = rolServices.buscarPorId(id);
	        return ResponseEntity.ok(rolDto);
	    } catch (EntityNotFoundException e) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("mensaje", e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    } catch (DataAccessException e) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("mensaje", "Error al acceder a la base de datos");
	        response.put("detalle", e.getMostSpecificCause().getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
