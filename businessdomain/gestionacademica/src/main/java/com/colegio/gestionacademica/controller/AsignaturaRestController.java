package com.colegio.gestionacademica.controller;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.entities.Asignatura;
import com.colegio.gestionacademica.entities.Curso;
import com.colegio.gestionacademica.repository.AsignaturaRepository;
import com.colegio.gestionacademica.repository.CursoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaRestController {

    private final AsignaturaRepository asignaturaRepository;
    private final CursoRepository cursoRepository;

    public AsignaturaRestController(AsignaturaRepository asignaturaRepository, CursoRepository cursoRepository) {
        this.asignaturaRepository = asignaturaRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Asignatura> list() {
        return asignaturaRepository.findAll();
    }

    @GetMapping("/por-curso/{cursoId}")
    public List<Asignatura> porCurso(@PathVariable Long cursoId) {
        return asignaturaRepository.findByCursoId(cursoId);
    }

    @PostMapping
    public ResponseEntity<Asignatura> post(@RequestParam Long cursoId, @org.springframework.web.bind.annotation.RequestBody Asignatura input) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new BusinessRuleException(
                        "GA-404", HttpStatus.NOT_FOUND, "Curso no encontrado: " + cursoId));
        input.setCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(asignaturaRepository.save(input));
    }
}
