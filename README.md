# Usuarios_Login
🧾 Descripción
Usuarios_Login es un sistema backend desarrollado en Spring Boot 3 que permite la gestión segura de usuarios, incluyendo autenticación con contraseña encriptada (BCrypt), asignación de roles, carga de fotos de perfil y funcionalidades extendibles como preguntas de seguridad.

Este sistema puede servir como base para aplicaciones más amplias que requieran autenticación, autorización y manejo seguro de datos de usuarios.

🚀 Tecnologías usadas
Java 17

Spring Boot 3

Spring Data JPA

PostgreSQL

BCrypt (para encriptación de contraseñas)

Maven

GitLab (repositorio)

⚙️ Instalación
Clona el repositorio:

	git clone https://gitlab.com/apis_rest/usuarios_login.git
	cd usuarios_login

Configura tu base de datos PostgreSQL y asegúrate de tener el archivo application.properties con los datos correctos.

Compila y ejecuta la aplicación:

	./mvnw spring-boot:run

🧪 Endpoints principales

    Roles
     Listar roles
		GET http://localhost:8081/prueba/listarRol
     Buscar rol por ID
        GET http://localhost:8081/prueba/roles/{id}


📦 Estructura del proyecto

	com.proyect.System_userAndLogin
	│
	├── controller
	│   ├── pruebas
	├── Dto
	│   ├── ContrasenaDto
	│   ├── FotoUsuarioDto
	│   ├── PreguntaSeguridadDto
	│   ├── RolDto
	│   └── UsuarioDto
	├── model
	│   ├── Contrasena
	│   ├── FotoUsuario
	│   ├── PreguntaSeguridad
	│   ├── Rol
	│   └── Usuario
	├── repository
	│   ├── IContrasenaRepository
	│   ├── IFotoUsuarioRepository
	│   ├── IPreguntaSeguridadRepository
	│   ├── IRolRepository
	│   └── IUsuarioRepository
	├── Response
	│   ├── PreguntaSerguridadResponse
	│   ├   ├── PreguntaSeguridadResponse
	│   ├   ├── PreguntaSeguridadResponseRest
	│   ├── ResponseContrasena
	│   ├   └── ContrasenaResponse	
	│   ├   ├── ContrasenaResponseRest
	│   ├── ResponseUsuario
	│   ├   ├── UsuarioResponse
	│   ├   ├── UsuarioResponseRest
	│   └── ResponseUsuario
	├── Services
	│   ├── IContrasenaRepositoryServices
	│   ├── IFotoUsuarioRepositoryServices
	│   ├── IPreguntaSeguridadRepositoryServices
	│   ├── IRolRepositoryServices
	│   └── IUsuarioServices
	├── ServicesImpl
	│   ├── ContrasenaRepositoryServicesImpl
	│   ├── FotoUsuarioRepositoryServicesImpl
	│   ├── PreguntaSeguridadRepositoryServicesImpl
	│   ├── RolRepositoryServicesImpl
	│   └── UsuarioServicesImpl
	├── utils
	│   └── util

📷 Visuales (opcional)
Se agregara captura posteriormente



🧑‍💻 Autor
Proyecto desarrollado por: EmmCast91
