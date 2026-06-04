package com.colegio.gestionacademica.controller;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.entities.Curso;
import com.colegio.gestionacademica.repository.CursoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curso")
public class CursoRestController {

    private final CursoRepository cursoRepository;

    public CursoRestController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Curso> list() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> get(@PathVariable Long id) {
        return cursoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessRuleException(
                        "GA-404", HttpStatus.NOT_FOUND, "Curso no encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<Curso> post(@RequestBody Curso input) {
        if (input.getAsignaturas() != null) {
            input.getAsignaturas().forEach(a -> a.setCurso(input));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoRepository.save(input));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> put(@PathVariable Long id, @RequestBody Curso input) {
        return cursoRepository.findById(id).map(existente -> {
            existente.setNombre(input.getNombre());
            existente.setNivel(input.getNivel());
            existente.setAnioLectivo(input.getAnioLectivo());
            return ResponseEntity.ok(cursoRepository.save(existente));
        }).orElseThrow(() -> new BusinessRuleException(
                "GA-404", HttpStatus.NOT_FOUND, "Curso no encontrado: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
