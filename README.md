# Plataforma Libro de Clases Digital — Colegio Bernardo O'Higgins

Sistema de microservicios para la gestión académica, de asistencia y de comunicaciones de un colegio. Construido con Spring Boot 4 y Spring Cloud.

## Arquitectura

```
                          Frontend (React)
                                │ HTTPS
                                ▼
                        API Gateway (8080)
            ┌───────────────────┼───────────────────┐
            ▼                   ▼                   ▼
   Gestión Académica      Asistencia y         Comunicaciones
        (8081)            Anotaciones (8082)       (8083)
            │                   │  │                 ▲
            ▼                   ▼  └── RabbitMQ ──────┘
       PostgreSQL          PostgreSQL  (eventos)  PostgreSQL
                                
   Eureka Server (8761) · Spring Boot Admin (8099) · Keycloak Adapter (8088)
```

## Módulos

### Dominio de negocio (`businessdomain/`)
| Servicio | Puerto | Responsabilidad |
|----------|--------|-----------------|
| **gestionacademica** | 8081 | Cursos, asignaturas, evaluaciones y notas |
| **asistencia** | 8082 | Asistencia y anotaciones; publica eventos a RabbitMQ; Circuit Breaker |
| **comunicaciones** | 8083 | Mensajes y notificaciones; consume eventos de RabbitMQ |

### Dominio de infraestructura (`infraestructuredomain/`)
| Componente | Puerto | Rol |
|-----------|--------|-----|
| **eurekaServer** | 8761 | Service Discovery |
| **apigateway** | 8080 | Punto de entrada único, enrutamiento y filtros de autenticación |
| **keycloakadapter** | 8088 | Validación de JWT y roles (apoyo de seguridad) |
| **springBootAdmin** | 8099 | Monitoreo de los servicios |

## Patrones de diseño implementados
1. **API Gateway** — entrada única (Spring Cloud Gateway).
2. **Service Discovery** — registro/descubrimiento dinámico (Eureka).
3. **Repository** — acceso a datos abstraído (Spring Data JPA) en los tres servicios.
4. **Factory Method** — creación de evaluaciones por tipo (`gestionacademica/factory`).
5. **Circuit Breaker** — resiliencia con fallback (Resilience4j en `asistencia`).
6. **Event-Driven / Mensajería asíncrona** — RabbitMQ entre Asistencia y Comunicaciones.

## Orden de arranque
1. **RabbitMQ** (Docker: `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`)
2. **eurekaServer** (8761)
3. **gestionacademica**, **asistencia**, **comunicaciones**
4. **apigateway** (8080)
5. (opcional) **springBootAdmin**, **keycloakadapter**

Cada servicio se levanta con `mvn spring-boot:run` desde su carpeta. Por defecto usan H2 en memoria; para PostgreSQL, agregar `-Dspring-boot.run.profiles=postgres`.

## Pruebas
```bash
# Desde la raíz, para todo el dominio de negocio:
cd businessdomain && mvn test
```

## Notas técnicas
- Spring Boot 4 renombró `spring-boot-starter-web` a `spring-boot-starter-webmvc`; el proyecto ya usa el nombre nuevo.
- El modelo "una base de datos por servicio" mantiene el aislamiento entre dominios (ningún servicio accede a la BD de otro).
