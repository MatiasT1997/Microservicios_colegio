# gestionacademica

Microservicio de gestion academica del sistema Libro de Clases Digital. Administra cursos, asignaturas, evaluaciones y notas.

## Responsabilidades

- CRUD de Curso, Asignatura, Evaluacion y Nota
- Persistencia mediante Spring Data JPA sobre PostgreSQL
- Registro en Eureka para ser descubierto por el API Gateway

## Requisitos previos

- Java 17
- Maven 3.9+
- PostgreSQL 17 corriendo en localhost:5432
- eurekaServer debe estar corriendo antes de iniciar este servicio

## Base de datos

Crear la base de datos antes de levantar el servicio:

```sql
CREATE DATABASE academica_db;
CREATE USER colegio WITH PASSWORD 'colegio';
GRANT ALL PRIVILEGES ON DATABASE academica_db TO colegio;
```

El esquema de tablas se genera y actualiza automaticamente mediante Hibernate (`spring.jpa.hibernate.ddl-auto=update`), no requiere scripts manuales.

## Instalacion y ejecucion

Desde la carpeta `gestionacademica`:

```
mvn clean install
mvn spring-boot:run
```

O bien, en NetBeans: abrir el proyecto y ejecutar Run sobre `gestionacademica`.

El servicio queda disponible en `http://localhost:8081`.

## Documentacion de la API (Swagger)

Una vez levantado el servicio:

```
http://localhost:8081/swagger-ui.html
```

## Endpoints principales

| Metodo | Ruta                              | Descripcion                       |
|--------|-----------------------------------|------------------------------------|
| GET    | /curso                            | Listar cursos                      |
| POST   | /curso                            | Crear curso                        |
| GET    | /asignatura                       | Listar asignaturas                 |
| GET    | /asignatura/curso/{cursoId}       | Listar asignaturas de un curso     |
| POST   | /asignatura                       | Crear asignatura                   |
| GET    | /evaluacion/asignatura/{id}       | Listar evaluaciones de una asignatura |
| POST   | /evaluacion/asignatura/{id}       | Crear evaluacion                   |
| GET    | /nota/estudiante/{estudianteId}   | Listar notas de un estudiante      |
| POST   | /nota/{evaluacionId}              | Registrar nota (rango 1.0 a 7.0)   |

Nota: cuando se accede a traves del API Gateway, todas estas rutas se prefijan con `/api/academica`.

## Pruebas

Ejecutar:
```
mvn test
```

Las pruebas se encuentran en `src/test/java/cl/duoc/colegio/gestionacademica`.
