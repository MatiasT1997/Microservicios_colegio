package com.colegio.comunicaciones.messaging;

import com.colegio.comunicaciones.config.RabbitMQConfig;
import com.colegio.comunicaciones.service.MensajeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de anotacion. Al recibir un evento desde RabbitMQ,
 * genera la notificacion correspondiente para el apoderado.
 */
@Component
public class AnotacionEventListener {

    private static final Logger log = LoggerFactory.getLogger(AnotacionEventListener.class);

    private final MensajeService mensajeService;

    public AnotacionEventListener(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACIONES)
    public void recibir(AnotacionEvent evento) {
        log.info("Evento de anotacion recibido: {} para estudiante {}",
                evento.getAnotacionId(), evento.getEstudianteNombre());
        mensajeService.crearDesdeAnotacion(evento);
    }
}
