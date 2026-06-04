package com.colegio.gestionacademica.controller;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.entities.Evaluacion;
import com.colegio.gestionacademica.entities.Nota;
import com.colegio.gestionacademica.repository.EvaluacionRepository;
import com.colegio.gestionacademica.repository.NotaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nota")
public class NotaRestController {

    private final NotaRepository notaRepository;
    private final EvaluacionRepository evaluacionRepository;

    public NotaRestController(NotaRepository notaRepository, EvaluacionRepository evaluacionRepository) {
        this.notaRepository = notaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @GetMapping("/por-evaluacion/{evaluacionId}")
    public List<Nota> porEvaluacion(@PathVariable Long evaluacionId) {
        return notaRepository.findByEvaluacionId(evaluacionId);
    }

    @GetMapping("/por-estudiante/{estudianteId}")
    public List<Nota> porEstudiante(@PathVariable Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }

    @PostMapping
    public ResponseEntity<Nota> registrar(@RequestParam Long evaluacionId, @RequestBody Nota input) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new BusinessRuleException(
                        "GA-404", HttpStatus.NOT_FOUND, "Evaluacion no encontrada: " + evaluacionId));
        if (input.getValor() != null && (input.getValor() < 1.0 || input.getValor() > 7.0)) {
            throw new BusinessRuleException(
                    "GA-422", HttpStatus.UNPROCESSABLE_ENTITY, "La nota debe estar entre 1.0 y 7.0");
        }
        input.setEvaluacion(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(notaRepository.save(input));
    }
}
