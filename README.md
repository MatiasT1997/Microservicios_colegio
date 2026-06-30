# Libro de Clases Digital - Colegio Bernardo O'Higgins

Plataforma de gestion academica desarrollada para la asignatura DSY1106 - Desarrollo Fullstack III (DUOC UC).

Arquitectura de microservicios compuesta por:

| Componente        | Puerto | Descripcion                                              |
|-------------------|--------|------------------------------------------------------------|
| front end         | 3000   | Interfaz en React (SPA)                                  |
| apigateway        | 8080   | API Gateway / BFF (Spring Cloud Gateway, WebFlux, JWT)    |
| eurekaServer      | 8761   | Service Discovery                                         |
| gestionacademica  | 8081   | Cursos, asignaturas, evaluaciones y notas                |
| asistencia        | 8082   | Asistencia diaria y anotaciones de conducta               |
| comunicaciones    | 8083   | Mensajes y notificaciones (consume eventos de RabbitMQ)    |

## Requisitos previos

- Java 17 (JDK Temurin recomendado)
- Maven 3.9+ (o usar el wrapper incluido en cada microservicio)
- Node.js 18+ y npm
- PostgreSQL 17
- Docker Desktop (para levantar RabbitMQ)
- NetBeans o cualquier IDE compatible con Maven

## Orden de ejecucion

Los servicios deben levantarse en este orden, esperando que cada uno termine de arrancar antes de iniciar el siguiente:

1. **PostgreSQL**: crear las bases de datos `academica_db`, `asistencia_db` y `comunicaciones_db`
2. **Docker / RabbitMQ**: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
3. **eurekaServer** (puerto 8761)
4. **gestionacademica**, **asistencia**, **comunicaciones** (en cualquier orden entre ellos)
5. **apigateway** (puerto 8080)
6. **front end** (puerto 3000)

Revisar el README.md de cada componente para instrucciones especificas de instalacion y ejecucion.

## Documentacion de la API

- Swagger UI de cada microservicio (una vez levantado):
  - Gestion Academica: http://localhost:8081/swagger-ui.html
  - Asistencia: http://localhost:8082/swagger-ui.html
  - Comunicaciones: http://localhost:8083/swagger-ui.html
- Coleccion Postman: `Libro_de_Clases_Digital.postman_collection.json` (en la raiz del proyecto)

## Autenticacion

Todas las rutas expuestas a traves del API Gateway bajo el prefijo `/api/**` requieren un token JWT.

1. Obtener el token:
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

2. Usar el token recibido en el encabezado de las siguientes peticiones:
```
Authorization: Bearer {token}
```

El frontend maneja esto automaticamente: al iniciar sesion guarda el token y lo adjunta en cada peticion hacia el Gateway.

## Pruebas

El informe y codigo de pruebas unitarias, de integracion y end to end se encuentra documentado por separado segun lo solicitado en la pauta de evaluacion (Informe de Pruebas Unitarias).

## Repositorios

Ver archivo `repositorios.txt` con los enlaces a cada repositorio GitHub del proyecto.
