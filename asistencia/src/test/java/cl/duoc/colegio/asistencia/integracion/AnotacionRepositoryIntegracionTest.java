package cl.duoc.colegio.asistencia.integracion;

import cl.duoc.colegio.asistencia.model.Anotacion;
import cl.duoc.colegio.asistencia.model.TipoAnotacion;
import cl.duoc.colegio.asistencia.repository.AnotacionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integracion - AnotacionRepository
 * Verifican las operaciones de persistencia sobre Anotacion
 * y los metodos de consulta personalizados con H2 en memoria.
 */
@DataJpaTest
@ActiveProfiles("test")
class AnotacionRepositoryIntegracionTest {

    @Autowired
    private AnotacionRepository anotacionRepository;

    private Anotacion crearAnotacion(Long estudianteId, TipoAnotacion tipo) {
        Anotacion a = new Anotacion();
        a.setEstudianteId(estudianteId);
        a.setApoderadoId(100L);
        a.setDescripcion("Descripcion de prueba");
        a.setTipo(tipo);
        a.setFecha(LocalDate.now());
        return a;
    }

    @Test
    void debeGuardarYRecuperarAnotacion() {
        Anotacion anotacion = crearAnotacion(1L, TipoAnotacion.GRAVE);

        Anotacion guardada = anotacionRepository.save(anotacion);

        assertNotNull(guardada.getId());
        assertEquals(TipoAnotacion.GRAVE, guardada.getTipo());
    }

    @Test
    void debeBuscarAnotacionesPorEstudiante() {
        anotacionRepository.save(crearAnotacion(1L, TipoAnotacion.GRAVE));
        anotacionRepository.save(crearAnotacion(1L, TipoAnotacion.NEGATIVA));
        anotacionRepository.save(crearAnotacion(2L, TipoAnotacion.POSITIVA));

        List<Anotacion> deEstudiante1 = anotacionRepository.findByEstudianteId(1L);

        assertEquals(2, deEstudiante1.size());
    }

    @Test
    void debeRetornarListaVaciaSiEstudianteNoTieneAnotaciones() {
        List<Anotacion> resultado = anotacionRepository.findByEstudianteId(999L);

        assertTrue(resultado.isEmpty());
    }
}
