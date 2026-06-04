package com.colegio.asistencia.controller;

import com.colegio.asistencia.service.ComunicacionesClient;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint que consulta a Comunicaciones via REST, demostrando el Circuit Breaker.
 */
@RestController
@RequestMapping("/integracion")
public class ConsultaComunicacionesController {

    private final ComunicacionesClient comunicacionesClient;

    public ConsultaComunicacionesController(ComunicacionesClient comunicacionesClient) {
        this.comunicacionesClient = comunicacionesClient;
    }

    @GetMapping("/mensajes-apoderado/{apoderadoId}")
    public List<Map<String, Object>> mensajesApoderado(@PathVariable Long apoderadoId) {
        return comunicacionesClient.obtenerMensajesApoderado(apoderadoId);
    }
}
