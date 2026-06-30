package cl.duoc.colegio.asistencia.messaging;

import cl.duoc.colegio.asistencia.model.Anotacion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnotacionEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publicar(AnotacionEvent evento) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            evento
        );
    }

    public void publicarAnotacionCreada(Anotacion anotacion) {
        AnotacionEvent evento = new AnotacionEvent(
            anotacion.getEstudianteId(),
            anotacion.getApoderadoId(),
            anotacion.getDescripcion(),
            anotacion.getTipo().name(),
            anotacion.getFecha()
        );
        publicar(evento);
    }
}
