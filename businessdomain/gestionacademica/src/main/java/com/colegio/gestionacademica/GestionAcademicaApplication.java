package com.colegio.gestionacademica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Gestion Academica.
 * Gestiona cursos, asignaturas, evaluaciones y notas del Colegio Bernardo O'Higgins.
 * Se registra en Eureka y queda accesible a traves del API Gateway.
 */
@SpringBootApplication
public class GestionAcademicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionAcademicaApplication.class, args);
    }
}
