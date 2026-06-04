package com.colegio.comunicaciones.service;

import com.colegio.comunicaciones.entities.Mensaje;
import com.colegio.comunicaciones.messaging.AnotacionEvent;
import com.colegio.comunicaciones.repository.MensajeRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Logica de comunicaciones: crea mensajes/notificaciones, incluyendo las
 * generadas automaticamente a partir de eventos de anotacion recibidos por RabbitMQ.
 */
@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public Mensaje crear(Mensaje mensaje) {
        if (mensaje.getFecha() == null) {
            mensaje.setFecha(LocalDateTime.now());
        }
        if (mensaje.getEstado() == null) {
            mensaje.setEstado(Mensaje.Estado.PENDIENTE);
        }
        return mensajeRepository.save(mensaje);
    }

    /**
     * Genera una notificacion para el apoderado a partir de un evento de anotacion.
     */
    public Mensaje crearDesdeAnotacion(AnotacionEvent evento) {
        Mensaje notificacion = new Mensaje();
        notificacion.setDestinatarioId(evento.getApoderadoId());
        notificacion.setDestinatarioNombre("Apoderado de " + evento.getEstudianteNombre());
        notificacion.setAsunto("Nueva anotacion " + evento.getTipo());
        notificacion.setContenido("Se ha registrado una anotacion para "
                + evento.getEstudianteNombre() + ": " + evento.getDescripcion());
        notificacion.setCanal(Mensaje.Canal.NOTIFICACION_APP);
        notificacion.setEstado(Mensaje.Estado.PENDIENTE);
        notificacion.setFecha(LocalDateTime.now());
        return mensajeRepository.save(notificacion);
    }
}
