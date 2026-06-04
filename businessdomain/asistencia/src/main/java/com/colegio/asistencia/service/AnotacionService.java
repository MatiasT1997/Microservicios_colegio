package com.colegio.asistencia.service;

import com.colegio.asistencia.entities.Anotacion;
import com.colegio.asistencia.messaging.AnotacionEvent;
import com.colegio.asistencia.messaging.AnotacionEventPublisher;
import com.colegio.asistencia.repository.AnotacionRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Logica de anotaciones. Al registrar una anotacion NEGATIVA o GRAVE publica un
 * evento a RabbitMQ para que Comunicaciones notifique al apoderado (asincrono).
 */
@Service
public class AnotacionService {

    private final AnotacionRepository anotacionRepository;
    private final AnotacionEventPublisher eventPublisher;

    public AnotacionService(AnotacionRepository anotacionRepository, AnotacionEventPublisher eventPublisher) {
        this.anotacionRepository = anotacionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Anotacion registrar(Anotacion anotacion) {
        if (anotacion.getFecha() == null) {
            anotacion.setFecha(LocalDateTime.now());
        }
        Anotacion guardada = anotacionRepository.save(anotacion);

        // Solo notificamos al apoderado en anotaciones negativas o graves
        if (guardada.getTipo() == Anotacion.Tipo.NEGATIVA || guardada.getTipo() == Anotacion.Tipo.GRAVE) {
            AnotacionEvent evento = new AnotacionEvent(
                    guardada.getId(),
                    guardada.getEstudianteId(),
                    guardada.getEstudianteNombre(),
                    guardada.getApoderadoId(),
                    guardada.getTipo().name(),
                    guardada.getDescripcion(),
                    guardada.getFecha());
            eventPublisher.publicar(evento);
        }
        return guardada;
    }
}
