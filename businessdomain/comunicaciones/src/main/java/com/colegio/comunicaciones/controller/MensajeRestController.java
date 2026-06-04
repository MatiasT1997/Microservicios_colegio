package com.colegio.comunicaciones.controller;

import com.colegio.comunicaciones.entities.Mensaje;
import com.colegio.comunicaciones.repository.MensajeRepository;
import com.colegio.comunicaciones.service.MensajeService;
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
@RequestMapping("/mensaje")
public class MensajeRestController {

    private final MensajeService mensajeService;
    private final MensajeRepository mensajeRepository;

    public MensajeRestController(MensajeService mensajeService, MensajeRepository mensajeRepository) {
        this.mensajeService = mensajeService;
        this.mensajeRepository = mensajeRepository;
    }

    @GetMapping
    public List<Mensaje> list() {
        return mensajeRepository.findAll();
    }

    @GetMapping("/por-apoderado/{apoderadoId}")
    public List<Mensaje> porApoderado(@PathVariable Long apoderadoId) {
        return mensajeRepository.findByDestinatarioId(apoderadoId);
    }

    @PostMapping
    public ResponseEntity<Mensaje> crear(@RequestBody Mensaje input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeService.crear(input));
    }
}
