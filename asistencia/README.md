# asistencia

Microservicio de asistencia y conducta del sistema Libro de Clases Digital. Administra el registro diario de asistencia y las anotaciones de conducta de los estudiantes.

## Responsabilidades

- CRUD de RegistroAsistencia y Anotacion
- Persistencia mediante Spring Data JPA sobre PostgreSQL
- Publicacion de eventos a RabbitMQ cuando se registra una anotacion de tipo NEGATIVA o GRAVE
- Circuit Breaker (Resilience4j) para proteger al sistema ante fallas del microservicio comunicaciones
- Registro en Eureka para ser descubierto por el API Gateway

## Requisitos previos

- Java 17
- Maven 3.9+
- PostgreSQL 17 corriendo en localhost:5432
- RabbitMQ corriendo en localhost:5672 (ver Docker mas abajo)
- eurekaServer debe estar corriendo antes de iniciar este servicio

## Base de datos

```sql
CREATE DATABASE asistencia_db;
CREATE USER colegio WITH PASSWORD 'colegio';
GRANT ALL PRIVILEGES ON DATABASE asistencia_db TO colegio;
```

El esquema se genera y actualiza automaticamente mediante Hibernate.

## RabbitMQ (Docker)

```
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Panel de administracion disponible en `http://localhost:15672` (usuario `guest`, contrasena `guest`).

## Instalacion y ejecucion

Desde la carpeta `asistencia`:

```
mvn clean install
mvn spring-boot:run
```

O bien, en NetBeans: abrir el proyecto y ejecutar Run sobre `asistencia`.

El servicio queda disponible en `http://localhost:8082`.

## Documentacion de la API (Swagger)

```
http://localhost:8082/swagger-ui.html
```

## Endpoints principales

| Metodo | Ruta                                  | Descripcion                            |
|--------|----------------------------------------|------------------------------------------|
| GET    | /asistencia                            | Listar registros de asistencia          |
| GET    | /asistencia/curso/{cursoId}            | Listar asistencia de un curso           |
| GET    | /asistencia/estudiante/{estudianteId}  | Listar asistencia de un estudiante      |
| POST   | /asistencia                            | Registrar asistencia                    |
| GET    | /anotacion                             | Listar anotaciones                      |
| GET    | /anotacion/estudiante/{estudianteId}   | Listar anotaciones de un estudiante     |
| POST   | /anotacion                             | Crear anotacion (GRAVE/NEGATIVA publica evento) |

Nota: cuando se accede a traves del API Gateway, todas estas rutas se prefijan con `/api/asistencia`.

## Flujo de notificacion automatica

Al registrar una anotacion con tipo `NEGATIVA` o `GRAVE`, este servicio publica un evento `anotacion.creada` al exchange `colegio.exchange` de RabbitMQ. El microservicio comunicaciones consume ese evento y genera la notificacion correspondiente de forma automatica.

## Pruebas

Ejecutar:
```
mvn test
```

Las pruebas se encuentran en `src/test/java/cl/duoc/colegio/asistencia`.
