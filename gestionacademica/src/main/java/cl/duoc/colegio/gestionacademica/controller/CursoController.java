package cl.duoc.colegio.gestionacademica.controller;

import cl.duoc.colegio.gestionacademica.model.Curso;
import cl.duoc.colegio.gestionacademica.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Curso obtener(@PathVariable Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Curso crear(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @PutMapping("/{id}")
    public Curso actualizar(@PathVariable Long id, @RequestBody Curso curso) {
        curso.setId(id);
        return cursoRepository.save(curso);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        cursoRepository.deleteById(id);
    }
}