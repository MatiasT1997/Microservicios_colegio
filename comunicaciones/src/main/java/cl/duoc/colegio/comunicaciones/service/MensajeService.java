package cl.duoc.colegio.comunicaciones.service;

import cl.duoc.colegio.comunicaciones.messaging.AnotacionEvent;
import cl.duoc.colegio.comunicaciones.model.CanalMensaje;
import cl.duoc.colegio.comunicaciones.model.EstadoMensaje;
import cl.duoc.colegio.comunicaciones.model.Mensaje;
import cl.duoc.colegio.comunicaciones.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public Mensaje crearDesdeEvento(AnotacionEvent evento) {
        Mensaje mensaje = new Mensaje();
        mensaje.setEstudianteId(evento.getEstudianteId());
        mensaje.setApoderadoId(evento.getApoderadoId());
        mensaje.setDescripcion(evento.getDescripcion());
        mensaje.setTipo(evento.getTipo());
        mensaje.setFechaAnotacion(evento.getFecha());
        mensaje.setCanal(CanalMensaje.SISTEMA);
        mensaje.setEstado(EstadoMensaje.ENVIADO);
        mensaje.setFechaEnvio(LocalDateTime.now());
        return mensajeRepository.save(mensaje);
    }

    public List<Mensaje> listarTodos() {
        return mensajeRepository.findAll();
    }
}