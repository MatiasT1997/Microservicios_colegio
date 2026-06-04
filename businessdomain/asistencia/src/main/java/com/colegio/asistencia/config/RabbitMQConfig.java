package com.colegio.asistencia.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de RabbitMQ (patron Event-Driven / mensajeria asincrona).
 *
 * Asistencia publica eventos de anotacion en un Topic Exchange. Comunicaciones
 * se suscribe a la cola enlazada para enviar la notificacion al apoderado, sin
 * que Asistencia tenga que esperar su respuesta.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "colegio.eventos";
    public static final String QUEUE_NOTIFICACIONES = "colegio.notificaciones";
    public static final String ROUTING_KEY_ANOTACION = "anotacion.creada";

    @Bean
    public TopicExchange eventosExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificacionesQueue() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    @Bean
    public Binding binding(Queue notificacionesQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(notificacionesQueue)
                .to(eventosExchange)
                .with("anotacion.*");
    }

    /** Serializa los eventos como JSON en lugar de la serializacion Java por defecto. */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
