package cl.duoc.colegio.comunicaciones.integracion;

import cl.duoc.colegio.comunicaciones.model.CanalMensaje;
import cl.duoc.colegio.comunicaciones.model.EstadoMensaje;
import cl.duoc.colegio.comunicaciones.model.Mensaje;
import cl.duoc.colegio.comunicaciones.repository.MensajeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integracion - MensajeRepository
 * Verifican la persistencia de Mensaje y los metodos
 * de consulta personalizados con H2 en memoria.
 */
@DataJpaTest
@ActiveProfiles("test")
class MensajeRepositoryIntegracionTest {

    @Autowired
    private MensajeRepository mensajeRepository;

    private Mensaje crearMensaje(Long estudianteId) {
        Mensaje m = new Mensaje();
        m.setEstudianteId(estudianteId);
        m.setApoderadoId(100L);
        m.setDescripcion("Conducta inapropiada");
        m.setTipo("GRAVE");
        m.setFechaAnotacion(LocalDate.now());
        m.setCanal(CanalMensaje.SISTEMA);
        m.setEstado(EstadoMensaje.ENVIADO);
        m.setFechaEnvio(LocalDateTime.now());
        return m;
    }

    @Test
    void debeGuardarMensajeCorrectamente() {
        Mensaje mensaje = crearMensaje(1L);

        Mensaje guardado = mensajeRepository.save(mensaje);

        assertNotNull(guardado.getId());
        assertEquals(CanalMensaje.SISTEMA, guardado.getCanal());
        assertEquals(EstadoMensaje.ENVIADO, guardado.getEstado());
    }

    @Test
    void debeListarTodosLosMensajes() {
        mensajeRepository.save(crearMensaje(1L));
        mensajeRepository.save(crearMensaje(2L));

        List<Mensaje> mensajes = mensajeRepository.findAll();

        assertEquals(2, mensajes.size());
    }

    @Test
    void debeBuscarMensajesPorEstudiante() {
        mensajeRepository.save(crearMensaje(1L));
        mensajeRepository.save(crearMensaje(1L));
        mensajeRepository.save(crearMensaje(2L));

        List<Mensaje> deEstudiante1 = mensajeRepository.findByEstudianteId(1L);

        assertEquals(2, deEstudiante1.size());
    }
}
