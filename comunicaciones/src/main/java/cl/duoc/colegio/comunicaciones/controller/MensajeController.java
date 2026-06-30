package cl.duoc.colegio.comunicaciones.controller;

import cl.duoc.colegio.comunicaciones.model.Mensaje;
import cl.duoc.colegio.comunicaciones.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mensaje")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping
    public List<Mensaje> listar() {
        return mensajeService.listarTodos();
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<Mensaje> listarPorEstudiante(@PathVariable Long estudianteId) {
        return mensajeService.listarTodos().stream()
                .filter(m -> estudianteId.equals(m.getEstudianteId()))
                .toList();
    }
}
