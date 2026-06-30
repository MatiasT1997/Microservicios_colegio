# comunicaciones

Microservicio de comunicaciones del sistema Libro de Clases Digital. Genera y administra las notificaciones automaticas hacia los apoderados.

## Responsabilidades

- Escucha eventos `anotacion.creada` publicados por el microservicio asistencia a traves de RabbitMQ
- Genera y persiste un Mensaje por cada evento recibido
- Expone un endpoint REST para listar las notificaciones generadas
- Registro en Eureka para ser descubierto por el API Gateway

## Requisitos previos

- Java 17
- Maven 3.9+
- PostgreSQL 17 corriendo en localhost:5432
- RabbitMQ corriendo en localhost:5672
- eurekaServer debe estar corriendo antes de iniciar este servicio

## Base de datos

```sql
CREATE DATABASE comunicaciones_db;
CREATE USER colegio WITH PASSWORD 'colegio';
GRANT ALL PRIVILEGES ON DATABASE comunicaciones_db TO colegio;
```

El esquema se genera y actualiza automaticamente mediante Hibernate.

## Instalacion y ejecucion

Desde la carpeta `comunicaciones`:

```
mvn clean install
mvn spring-boot:run
```

O bien, en NetBeans: abrir el proyecto y ejecutar Run sobre `comunicaciones`.

El servicio queda disponible en `http://localhost:8083`.

## Documentacion de la API (Swagger)

```
http://localhost:8083/swagger-ui.html
```

## Endpoints principales

| Metodo | Ruta                                | Descripcion                          |
|--------|--------------------------------------|----------------------------------------|
| GET    | /mensaje                             | Listar todas las notificaciones        |
| GET    | /mensaje/estudiante/{estudianteId}   | Listar notificaciones de un estudiante |

Nota: cuando se accede a traves del API Gateway, estas rutas se prefijan con `/api/comunicaciones`.

## Mensajeria

Este servicio escucha la cola `colegio.notificaciones`, vinculada al exchange `colegio.exchange` mediante la routing key `anotacion.creada`. Utiliza `Jackson2JsonMessageConverter` para deserializar los eventos en formato JSON.

## Pruebas

Ejecutar:
```
mvn test
```

Las pruebas se encuentran en `src/test/java/cl/duoc/colegio/comunicaciones`.
