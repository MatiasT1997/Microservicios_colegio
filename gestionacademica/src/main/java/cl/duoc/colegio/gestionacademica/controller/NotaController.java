package cl.duoc.colegio.gestionacademica.controller;

import cl.duoc.colegio.gestionacademica.exception.BusinessRuleException;
import cl.duoc.colegio.gestionacademica.model.Nota;
import cl.duoc.colegio.gestionacademica.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/nota")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @GetMapping
    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Nota obtener(@PathVariable Long id) {
        return notaRepository.findById(id).orElse(null);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<Nota> listarPorEstudiante(@PathVariable Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }

    @PostMapping("/{evaluacionId}")
    public Nota registrar(@PathVariable Long evaluacionId, @RequestBody Nota nota) {
        validarRango(nota);
        return notaRepository.save(nota);
    }

    @PutMapping("/{id}")
    public Nota actualizar(@PathVariable Long id, @RequestBody Nota nota) {
        validarRango(nota);
        nota.setId(id);
        return notaRepository.save(nota);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        notaRepository.deleteById(id);
    }

    private void validarRango(Nota nota) {
        if (nota.getValor() < 1.0 || nota.getValor() > 7.0) {
            throw new BusinessRuleException("La nota debe estar entre 1.0 y 7.0");
        }
    }
}