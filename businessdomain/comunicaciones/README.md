# Microservicio: Comunicaciones

Gestiona **mensajes y notificaciones** hacia apoderados, docentes y estudiantes. **Consume eventos de RabbitMQ** publicados por Asistencia: al recibir un evento de anotación, genera automáticamente una notificación para el apoderado.

- **Puerto:** 8083
- **Base de datos:** PostgreSQL (`comunicaciones_db`) — perfil por defecto H2 en memoria
- **Patrones / mecanismos aplicados:**
  - **Repository** (Spring Data JPA).
  - **Event-Driven** (RabbitMQ): consumidor con `@RabbitListener`.

## Requisitos
- Java 17+, Maven 3.9+
- Eureka Server en `http://localhost:8761`
- **RabbitMQ** en `localhost:5672`
- (Opcional) PostgreSQL para el perfil `postgres`

## Cómo ejecutar
```bash
mvn spring-boot:run
# o con PostgreSQL:
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/mensaje` | Lista todos los mensajes |
| GET | `/mensaje/por-apoderado/{id}` | Mensajes de un destinatario |
| POST | `/mensaje` | Crea un mensaje manualmente |

### Flujo asíncrono (end-to-end)
1. Asistencia registra una anotación GRAVE → publica `AnotacionEvent` al exchange `colegio.eventos`.
2. Comunicaciones (este servicio) escucha la cola `colegio.notificaciones`.
3. El listener crea una notificación para el apoderado, consultable en `/mensaje/por-apoderado/{apoderadoId}`.

## Pruebas
```bash
mvn test
```
Incluye `MensajeServiceTest` (generación de notificación desde evento).
