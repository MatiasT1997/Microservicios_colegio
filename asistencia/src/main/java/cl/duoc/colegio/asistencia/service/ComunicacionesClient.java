package cl.duoc.colegio.asistencia.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
public class ComunicacionesClient {

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "comunicacionesService", fallbackMethod = "fallback")
    public List<Map<String, Object>> obtenerMensajesPorEstudiante(Long estudianteId) {
        Map[] respuesta = restTemplate.getForObject(
            "http://comunicaciones/mensaje/estudiante/" + estudianteId,
            Map[].class
        );
        return respuesta != null ? List.of(respuesta) : List.of();
    }

    public List<Map<String, Object>> fallback(Long estudianteId, Throwable t) {
        return List.of();
    }
}