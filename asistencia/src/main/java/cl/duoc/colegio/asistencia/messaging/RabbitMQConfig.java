package cl.duoc.colegio.asistencia.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "colegio.exchange";
    public static final String QUEUE_NOTIFICACIONES = "colegio.notificaciones";
    public static final String ROUTING_KEY = "anotacion.creada";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queueNotificaciones() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    @Bean
    public Binding binding(Queue queueNotificaciones, TopicExchange exchange) {
        return BindingBuilder.bind(queueNotificaciones).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
