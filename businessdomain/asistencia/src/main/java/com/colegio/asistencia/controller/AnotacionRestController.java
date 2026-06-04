package com.colegio.asistencia.controller;

import com.colegio.asistencia.entities.Anotacion;
import com.colegio.asistencia.repository.AnotacionRepository;
import com.colegio.asistencia.service.AnotacionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anotacion")
public class AnotacionRestController {

    private final AnotacionService anotacionService;
    private final AnotacionRepository anotacionRepository;

    public AnotacionRestController(AnotacionService anotacionService, AnotacionRepository anotacionRepository) {
        this.anotacionService = anotacionService;
        this.anotacionRepository = anotacionRepository;
    }

    @GetMapping("/por-estudiante/{estudianteId}")
    public List<Anotacion> porEstudiante(@PathVariable Long estudianteId) {
        return anotacionRepository.findByEstudianteId(estudianteId);
    }

    /** Registra la anotacion y, si corresponde, dispara la notificacion asincrona. */
    @PostMapping
    public ResponseEntity<Anotacion> registrar(@RequestBody Anotacion input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anotacionService.registrar(input));
    }
}
