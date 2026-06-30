package cl.duoc.colegio.comunicaciones.messaging;

import cl.duoc.colegio.comunicaciones.service.MensajeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnotacionEventListener {

    @Autowired
    private MensajeService mensajeService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACIONES)
    public void onAnotacionCreada(AnotacionEvent evento) {
        mensajeService.crearDesdeEvento(evento);
    }
}
