package cl.duoc.colegio.asistencia.unitarias;

import cl.duoc.colegio.asistencia.messaging.AnotacionEventPublisher;
import cl.duoc.colegio.asistencia.model.Anotacion;
import cl.duoc.colegio.asistencia.model.TipoAnotacion;
import cl.duoc.colegio.asistencia.repository.AnotacionRepository;
import cl.duoc.colegio.asistencia.service.AnotacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias - Logica de eventos en AnotacionService
 * Verifican que el servicio publique eventos a RabbitMQ
 * SOLO para anotaciones de tipo NEGATIVA o GRAVE.
 */
@ExtendWith(MockitoExtension.class)
class AnotacionServiceTest {

    @Mock
    private AnotacionRepository anotacionRepo;

    @Mock
    private AnotacionEventPublisher publisher;

    @InjectMocks
    private AnotacionService anotacionService;

    private Anotacion crearAnotacion(TipoAnotacion tipo) {
        Anotacion a = new Anotacion();
        a.setEstudianteId(1L);
        a.setApoderadoId(100L);
        a.setDescripcion("Descripcion de prueba");
        a.setTipo(tipo);
        a.setFecha(LocalDate.now());
        return a;
    }

    @Test
    void debePublicarEventoCuandoAnotacionEsGrave() {
        Anotacion anotacion = crearAnotacion(TipoAnotacion.GRAVE);
        when(anotacionRepo.save(anotacion)).thenReturn(anotacion);

        anotacionService.registrar(anotacion);

        verify(publisher, times(1)).publicarAnotacionCreada(anotacion);
    }

    @Test
    void debePublicarEventoCuandoAnotacionEsNegativa() {
        Anotacion anotacion = crearAnotacion(TipoAnotacion.NEGATIVA);
        when(anotacionRepo.save(anotacion)).thenReturn(anotacion);

        anotacionService.registrar(anotacion);

        verify(publisher, times(1)).publicarAnotacionCreada(anotacion);
    }

    @Test
    void noDebePublicarEventoCuandoAnotacionEsPositiva() {
        Anotacion anotacion = crearAnotacion(TipoAnotacion.POSITIVA);
        when(anotacionRepo.save(anotacion)).thenReturn(anotacion);

        anotacionService.registrar(anotacion);

        verify(publisher, never()).publicarAnotacionCreada(any());
    }

    @Test
    void debeGuardarAnotacionIndependientementeDelTipo() {
        Anotacion anotacion = crearAnotacion(TipoAnotacion.POSITIVA);
        when(anotacionRepo.save(anotacion)).thenReturn(anotacion);

        anotacionService.registrar(anotacion);

        verify(anotacionRepo, times(1)).save(anotacion);
    }
}
