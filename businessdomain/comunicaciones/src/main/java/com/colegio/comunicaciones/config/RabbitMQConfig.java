package com.colegio.comunicaciones.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion del consumidor de RabbitMQ. Declara la misma cola que usa el
 * productor (Asistencia) y el conversor JSON para deserializar los eventos.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NOTIFICACIONES = "colegio.notificaciones";

    @Bean
    public Queue notificacionesQueue() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
