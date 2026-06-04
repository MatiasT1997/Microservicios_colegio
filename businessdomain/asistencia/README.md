# Microservicio: Asistencia y Anotaciones

Registra la **asistencia diaria** y las **anotaciones de conducta** de los estudiantes. Cuando se registra una anotación negativa o grave, **publica un evento a RabbitMQ** para que el microservicio Comunicaciones notifique al apoderado de forma asíncrona.

- **Puerto:** 8082
- **Base de datos:** PostgreSQL (`asistencia_db`) — perfil por defecto H2 en memoria
- **Patrones / mecanismos aplicados:**
  - **Repository** (Spring Data JPA).
  - **Circuit Breaker** (Resilience4j) en `ComunicacionesClient`, **con `fallbackMethod`**: si Comunicaciones está caído, devuelve una respuesta degradada en lugar de fallar.
  - **Event-Driven / mensajería asíncrona** (RabbitMQ) para desacoplar la notificación a apoderados.

## Requisitos
- Java 17+, Maven 3.9+
- Eureka Server en `http://localhost:8761`
- **RabbitMQ** en `localhost:5672` (usuario/clave `guest`/`guest`)
- (Opcional) PostgreSQL para el perfil `postgres`

### Levantar RabbitMQ rápido con Docker
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
# Panel de administración: http://localhost:15672
```

## Cómo ejecutar
```bash
mvn spring-boot:run
# o con PostgreSQL:
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/asistencia` | Registra asistencia (PRESENTE, AUSENTE, ATRASADO, JUSTIFICADO) |
| GET | `/asistencia/por-estudiante/{id}` | Asistencia de un estudiante |
| POST | `/anotacion` | Registra una anotación (publica evento si es NEGATIVA/GRAVE) |
| GET | `/anotacion/por-estudiante/{id}` | Anotaciones de un estudiante |
| GET | `/integracion/mensajes-apoderado/{id}` | Consulta a Comunicaciones (protegido por Circuit Breaker) |

### Ejemplo: anotación grave que dispara notificación
```bash
curl -X POST http://localhost:8082/anotacion \
  -H "Content-Type: application/json" \
  -d '{"estudianteId":1,"estudianteNombre":"Juan Perez","apoderadoId":50,"tipo":"GRAVE","descripcion":"Inasistencia reiterada","docente":"Prof. Rojas"}'
```

## Probar el Circuit Breaker
Con Comunicaciones apagado, llamar a `/integracion/mensajes-apoderado/50` varias veces: tras superar el umbral de fallos, el circuito se abre y se devuelve una lista vacía (fallback) sin lanzar error. El estado del circuito es visible en `/actuator/health`.

## Pruebas
```bash
mvn test
```
Incluye `AnotacionServiceTest` (verifica publicación de eventos) y `ComunicacionesClientTest` (verifica el fallback).
