# Microservicio: Gestión Académica

Gestiona **cursos, asignaturas, evaluaciones y notas** del Colegio Bernardo O'Higgins.

- **Puerto:** 8081
- **Base de datos:** PostgreSQL (`academica_db`) — perfil por defecto H2 en memoria para pruebas rápidas
- **Patrones de diseño aplicados:**
  - **Repository** (Spring Data JPA) para el acceso a datos.
  - **Factory Method** para la creación de evaluaciones según su tipo (`PRUEBA`, `TAREA`, `EXAMEN`, `CONTROL`). Ver paquete `factory`.
  - **DTO** en las peticiones de creación de evaluaciones.

## Requisitos
- Java 17+
- Maven 3.9+
- Eureka Server corriendo en `http://localhost:8761`
- (Opcional) PostgreSQL si se usa el perfil `postgres`

## Cómo ejecutar

Con H2 (sin BD externa):
```bash
mvn spring-boot:run
```

Con PostgreSQL:
```bash
# Crear la BD: createdb academica_db
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/curso` | Lista todos los cursos |
| POST | `/curso` | Crea un curso (con asignaturas anidadas) |
| GET | `/curso/{id}` | Obtiene un curso |
| POST | `/asignatura?cursoId={id}` | Crea una asignatura en un curso |
| GET | `/asignatura/por-curso/{cursoId}` | Asignaturas de un curso |
| POST | `/evaluacion` | Crea una evaluación (usa Factory Method) |
| GET | `/evaluacion/por-asignatura/{id}` | Evaluaciones de una asignatura |
| POST | `/nota?evaluacionId={id}` | Registra una nota (valida escala 1.0–7.0) |
| GET | `/nota/por-estudiante/{id}` | Notas de un estudiante |

### Ejemplo: crear una evaluación
```bash
curl -X POST http://localhost:8081/evaluacion \
  -H "Content-Type: application/json" \
  -d '{"asignaturaId":1,"tipo":"PRUEBA","titulo":"Prueba Unidad 1","ponderacion":30,"fecha":"2026-05-10"}'
```

## Pruebas
```bash
mvn test
```
Incluye pruebas del patrón Factory Method (`EvaluacionFactoryTest`) y del servicio con mocks (`EvaluacionServiceTest`).

## Documentación de la API
Con la app corriendo: `http://localhost:8081/swagger-ui.html`
