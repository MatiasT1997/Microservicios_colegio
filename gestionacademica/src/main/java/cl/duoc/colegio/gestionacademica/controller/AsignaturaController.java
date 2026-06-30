package cl.duoc.colegio.gestionacademica.controller;

import cl.duoc.colegio.gestionacademica.model.Asignatura;
import cl.duoc.colegio.gestionacademica.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @GetMapping
    public List<Asignatura> listar() {
        return asignaturaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Asignatura obtener(@PathVariable Long id) {
        return asignaturaRepository.findById(id).orElse(null);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Asignatura> listarPorCurso(@PathVariable Long cursoId) {
        return asignaturaRepository.findByCursoId(cursoId);
    }

    @PostMapping
    public Asignatura crear(@RequestBody Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }

    @PutMapping("/{id}")
    public Asignatura actualizar(@PathVariable Long id, @RequestBody Asignatura asignatura) {
        asignatura.setId(id);
        return asignaturaRepository.save(asignatura);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        asignaturaRepository.deleteById(id);
    }
}