package cl.duoc.colegio.asistencia.controller;

import cl.duoc.colegio.asistencia.model.Anotacion;
import cl.duoc.colegio.asistencia.service.AnotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/anotacion")
public class AnotacionController {

    @Autowired
    private AnotacionService anotacionService;

    @GetMapping
    public List<Anotacion> listar() {
        return anotacionService.listar();
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<Anotacion> listarPorEstudiante(@PathVariable Long estudianteId) {
        return anotacionService.listarPorEstudiante(estudianteId);
    }

    @PostMapping
    public Anotacion crear(@RequestBody Anotacion anotacion) {
        return anotacionService.registrar(anotacion);
    }
}