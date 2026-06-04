package com.colegio.asistencia.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Cliente REST hacia el microservicio Comunicaciones, protegido con Circuit Breaker.
 *
 * A diferencia del codigo base original, aqui SI se define un {@code fallbackMethod}:
 * si Comunicaciones esta caido o lento, el circuito se abre y se devuelve una
 * respuesta degradada en lugar de propagar el error. Asi Asistencia sigue operando
 * (resiliencia), tal como describe el informe.
 */
@Service
public class ComunicacionesClient {

    private static final Logger log = LoggerFactory.getLogger(ComunicacionesClient.class);

    private final WebClient.Builder webClientBuilder;

    public ComunicacionesClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Consulta los mensajes/notificaciones recientes de un apoderado.
     * Protegido por Circuit Breaker; ante fallo se ejecuta {@link #fallbackMensajes}.
     */
    @CircuitBreaker(name = "comunicacionesCB", fallbackMethod = "fallbackMensajes")
    public List<Map<String, Object>> obtenerMensajesApoderado(Long apoderadoId) {
        return webClientBuilder.build()
                .get()
                .uri("http://COMUNICACIONES/mensaje/por-apoderado/{id}", apoderadoId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();
    }

    /** Metodo de respaldo: respuesta degradada cuando Comunicaciones no responde. */
    @SuppressWarnings("unused")
    private List<Map<String, Object>> fallbackMensajes(Long apoderadoId, Throwable t) {
        log.warn("Circuit Breaker activo: Comunicaciones no disponible para apoderado {}. Causa: {}",
                apoderadoId, t.toString());
        return Collections.emptyList();
    }
}
