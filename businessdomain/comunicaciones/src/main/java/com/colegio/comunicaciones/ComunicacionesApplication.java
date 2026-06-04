package com.colegio.comunicaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Comunicaciones.
 * Gestiona mensajes y notificaciones; consume eventos de anotacion desde RabbitMQ
 * (publicados por Asistencia) para generar notificaciones a apoderados.
 */
@SpringBootApplication
public class ComunicacionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComunicacionesApplication.class, args);
    }
}
