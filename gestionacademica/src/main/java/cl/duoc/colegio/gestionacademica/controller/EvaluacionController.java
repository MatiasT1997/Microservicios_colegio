package cl.duoc.colegio.gestionacademica.controller;

import cl.duoc.colegio.gestionacademica.factory.EvaluacionFactory;
import cl.duoc.colegio.gestionacademica.factory.EvaluacionFactoryProvider;
import cl.duoc.colegio.gestionacademica.model.Asignatura;
import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import cl.duoc.colegio.gestionacademica.model.TipoEvaluacion;
import cl.duoc.colegio.gestionacademica.repository.AsignaturaRepository;
import cl.duoc.colegio.gestionacademica.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionController {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private EvaluacionFactoryProvider factoryProvider;

    @GetMapping
    public List<Evaluacion> listar() {
        return evaluacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Evaluacion obtener(@PathVariable Long id) {
        return evaluacionRepository.findById(id).orElse(null);
    }

    @GetMapping("/asignatura/{asignaturaId}")
    public List<Evaluacion> listarPorAsignatura(@PathVariable Long asignaturaId) {
        return evaluacionRepository.findByAsignaturaId(asignaturaId);
    }

    @PostMapping("/asignatura/{asignaturaId}")
    public Evaluacion crear(@PathVariable Long asignaturaId, @RequestBody Map<String, Object> body) {
        TipoEvaluacion tipo = TipoEvaluacion.valueOf((String) body.get("tipo"));
        String titulo = (String) body.get("titulo");
        Double ponderacion = ((Number) body.get("ponderacion")).doubleValue();
        LocalDate fecha = body.get("fecha") != null ? LocalDate.parse((String) body.get("fecha")) : null;

        EvaluacionFactory factory = factoryProvider.getFactory(tipo);
        Evaluacion evaluacion = factory.construir(titulo, ponderacion, fecha);

        Asignatura asignatura = asignaturaRepository.findById(asignaturaId).orElse(null);
        evaluacion.setAsignatura(asignatura);

        return evaluacionRepository.save(evaluacion);
    }

    @PutMapping("/{id}")
    public Evaluacion actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        evaluacion.setId(id);
        return evaluacionRepository.save(evaluacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        evaluacionRepository.deleteById(id);
    }
}