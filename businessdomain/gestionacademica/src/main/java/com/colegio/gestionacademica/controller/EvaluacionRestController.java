package com.colegio.gestionacademica.controller;

import com.colegio.gestionacademica.entities.Evaluacion;
import com.colegio.gestionacademica.entities.Evaluacion.TipoEvaluacion;
import com.colegio.gestionacademica.repository.EvaluacionRepository;
import com.colegio.gestionacademica.service.EvaluacionService;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionRestController {

    private final EvaluacionService evaluacionService;
    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionRestController(EvaluacionService evaluacionService, EvaluacionRepository evaluacionRepository) {
        this.evaluacionService = evaluacionService;
        this.evaluacionRepository = evaluacionRepository;
    }

    /** DTO de entrada para crear una evaluacion. */
    @Data
    public static class CrearEvaluacionRequest {
        private Long asignaturaId;
        private TipoEvaluacion tipo;
        private String titulo;
        private Double ponderacion;
        private LocalDate fecha;
    }

    @GetMapping
    public List<Evaluacion> list() {
        return evaluacionRepository.findAll();
    }

    @GetMapping("/por-asignatura/{asignaturaId}")
    public List<Evaluacion> porAsignatura(@PathVariable Long asignaturaId) {
        return evaluacionRepository.findByAsignaturaId(asignaturaId);
    }

    /**
     * Crea una evaluacion. El tipo determina la fabrica concreta usada
     * internamente (patron Factory Method).
     */
    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody CrearEvaluacionRequest req) {
        Evaluacion creada = evaluacionService.crearEvaluacion(
                req.getAsignaturaId(), req.getTipo(), req.getTitulo(),
                req.getPonderacion(), req.getFecha());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}
