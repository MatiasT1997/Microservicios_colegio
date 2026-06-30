package cl.duoc.colegio.asistencia.service;

import cl.duoc.colegio.asistencia.messaging.AnotacionEventPublisher;
import cl.duoc.colegio.asistencia.model.Anotacion;
import cl.duoc.colegio.asistencia.model.TipoAnotacion;
import cl.duoc.colegio.asistencia.repository.AnotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnotacionService {

    @Autowired
    private AnotacionRepository anotacionRepo;

    @Autowired
    private AnotacionEventPublisher publisher;

    public Anotacion registrar(Anotacion anotacion) {
        anotacionRepo.save(anotacion);

        if (anotacion.getTipo() == TipoAnotacion.NEGATIVA || anotacion.getTipo() == TipoAnotacion.GRAVE) {
            publisher.publicarAnotacionCreada(anotacion);
        }

        return anotacion;
    }

    public List<Anotacion> listar() {
        return anotacionRepo.findAll();
    }

    public List<Anotacion> listarPorEstudiante(Long estudianteId) {
        return anotacionRepo.findByEstudianteId(estudianteId);
    }
}