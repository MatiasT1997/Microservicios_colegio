package cl.duoc.colegio.asistencia.controller;

import cl.duoc.colegio.asistencia.model.RegistroAsistencia;
import cl.duoc.colegio.asistencia.repository.RegistroAsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asistencia")
public class RegistroAsistenciaController {

    @Autowired
    private RegistroAsistenciaRepository registroRepository;

    @GetMapping
    public List<RegistroAsistencia> listar() {
        return registroRepository.findAll();
    }

    @GetMapping("/curso/{cursoId}")
    public List<RegistroAsistencia> listarPorCurso(@PathVariable Long cursoId) {
        return registroRepository.findByCursoId(cursoId);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<RegistroAsistencia> listarPorEstudiante(@PathVariable Long estudianteId) {
        return registroRepository.findByEstudianteId(estudianteId);
    }

    @PostMapping
    public RegistroAsistencia crear(@RequestBody RegistroAsistencia registro) {
        return registroRepository.save(registro);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        registroRepository.deleteById(id);
    }
}