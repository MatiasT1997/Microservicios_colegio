package com.colegio.asistencia.controller;

import com.colegio.asistencia.entities.RegistroAsistencia;
import com.colegio.asistencia.repository.RegistroAsistenciaRepository;
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
@RequestMapping("/asistencia")
public class AsistenciaRestController {

    private final RegistroAsistenciaRepository repository;

    public AsistenciaRestController(RegistroAsistenciaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RegistroAsistencia> list() {
        return repository.findAll();
    }

    @GetMapping("/por-estudiante/{estudianteId}")
    public List<RegistroAsistencia> porEstudiante(@PathVariable Long estudianteId) {
        return repository.findByEstudianteId(estudianteId);
    }

    @GetMapping("/por-curso/{cursoId}")
    public List<RegistroAsistencia> porCurso(@PathVariable Long cursoId) {
        return repository.findByCursoId(cursoId);
    }

    @PostMapping
    public ResponseEntity<RegistroAsistencia> registrar(@RequestBody RegistroAsistencia input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(input));
    }
}
