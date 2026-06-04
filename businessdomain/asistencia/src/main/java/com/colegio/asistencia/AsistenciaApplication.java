package com.colegio.asistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Microservicio Asistencia y Anotaciones.
 * Registra asistencia y conducta; al registrar una anotacion grave publica un
 * evento a RabbitMQ para que Comunicaciones notifique al apoderado de forma
 * asincrona. Tambien puede consultar a Comunicaciones por REST, protegido con
 * Circuit Breaker.
 */
@SpringBootApplication
public class AsistenciaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsistenciaApplication.class, args);
    }

    /** WebClient con balanceo de carga via Eureka (lb://). */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
