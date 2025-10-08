package com.proyect.System_userAndLogin.Util;


import com.proyect.System_userAndLogin.Model.Rol;
import com.proyect.System_userAndLogin.Repository.IRolRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 Inicializador de la tabla {@code roles}.

 Este componente siembra (seed) un conjunto fijo de roles al arrancar la
 aplicación. La operación es idempotente: sólo inserta aquellos
 roles que aún no existen en la base de datos.
 
 
 Funcionamiento
 
 Al iniciar el contexto de Spring, se ejecuta {@link #init()}.
 Para cada nombre en {@link #ROLES}, verifica su existencia con
        {@code IRolRepository#existsByRol(String)}.
 Si no existe, lo crea mediante {@code save}.
 
 
 Requisitos
 
 	Que el repositorio exponga {@code boolean existsByRol(String rol)} o equivalente.
 	Recomendado: restricción única en BD para blindar duplicados:
 		ALTER TABLE roles ADD CONSTRAINT uq_roles_rol UNIQUE (rol);
	
	Configuración
 		
 		Propiedad app.seed.roles (boolean) para habilitar/deshabilitar el seed.
        Por defecto true.
 
 	Buenas prácticas
 	
 		Mantén esta lista sincronizada con tu modelo de autorización.
 		En proyectos grandes, considera migraciones (Flyway/Liquibase) para versionar datos semilla.</li>
 
 
 @author Emmanuel
 @version 1.5
 @since 2025-10
 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RolesInitializer {

    private final IRolRepository rolRepository;

    /**
     Lista fija de nombres de rol a sembrar.
     Ajusta según los roles oficiales de tu sistema.
     */
    private static final List<String> ROLES = List.of(
            "Admin_Doc",
            "Admin_User",
            "Recepcionista",
            "Veterinario",
            "Vendedor",
            "Cliente",
            "user",
            "pruenba"
    );

    /**
     Bandera para activar/desactivar el sembrado de roles.
     Configurable con la propiedad {@code app.seed.roles}.
     */
    @Value("${app.seed.roles:true}")
    private boolean enabled;

    /**
     Ejecuta el sembrado de roles al iniciar el contexto de Spring.
     
     El método es transaccional: todos los inserts (si los hay) se ejecutan en
     una sola transacción. Si {@link #enabled} es {@code false}, no realiza cambios.
     
     */
    @PostConstruct
    @Transactional
    public void init() {
        if (!enabled) {
            log.info("[SEED] Roles desactivado por configuración (app.seed.roles=false)");
            return;
        }

        int creados = 0;

        for (String nombre : ROLES) {
            // Requiere que IRolRepository exponga existsByRol(String)
            if (!rolRepository.existsByRol(nombre)) {
                // Crea la entidad rol. Usa setters para no depender de un constructor específico.
                Rol rol = new Rol();
                rol.setRol(nombre);
                rolRepository.save(rol);
                creados++;
            }
        }

        if (creados == 0) {
            log.info("[SEED] Roles: no había pendientes (0 creados)");
        } else {
            log.info("[SEED] Roles: creados {}", creados);
        }
    }
}




