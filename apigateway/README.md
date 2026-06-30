# apigateway

API Gateway (BFF) del sistema Libro de Clases Digital. Construido con Spring Cloud Gateway sobre WebFlux.

## Responsabilidades

- Punto unico de entrada para el frontend
- Autenticacion: emite y valida tokens JWT
- Enrutamiento hacia los microservicios de dominio usando Eureka (descubrimiento por nombre, sin direcciones fijas)
- CORS habilitado para el frontend (http://localhost:3000)

## Requisitos previos

- Java 17
- Maven 3.9+
- eurekaServer debe estar corriendo en el puerto 8761 antes de iniciar este servicio

## Instalacion y ejecucion

Desde la carpeta `apigateway`:

```
mvn clean install
mvn spring-boot:run
```

O bien, en NetBeans: abrir el proyecto y ejecutar Run sobre `apigateway`.

El servicio queda disponible en `http://localhost:8080`.

## Rutas expuestas

| Ruta                          | Microservicio destino | Puerto real |
|-------------------------------|------------------------|--------------|
| /auth/login                   | apigateway (local)     | 8080         |
| /api/academica/**             | gestionacademica       | 8081         |
| /api/asistencia/**            | asistencia             | 8082         |
| /api/comunicaciones/**        | comunicaciones         | 8083         |

## Autenticacion

```
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta:
```
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Todas las rutas bajo `/api/**` requieren el encabezado:
```
Authorization: Bearer {token}
```

Si el token falta o es invalido, el Gateway responde 401 Unauthorized antes de reenviar la peticion al microservicio correspondiente.

## Pruebas

Ejecutar:
```
mvn test
```

Las pruebas se encuentran en `src/test/java/cl/duoc/colegio/apigateway`.
