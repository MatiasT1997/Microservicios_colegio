package com.colegio.asistencia.messaging;

import com.colegio.asistencia.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de anotacion al exchange de RabbitMQ.
 */
@Component
public class AnotacionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AnotacionEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public AnotacionEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(AnotacionEvent evento) {
        log.info("Publicando evento de anotacion {} para estudiante {}",
                evento.getAnotacionId(), evento.getEstudianteNombre());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_ANOTACION,
                evento);
    }
}
