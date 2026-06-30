package cl.duoc.colegio.comunicaciones.e2e;

import cl.duoc.colegio.comunicaciones.model.CanalMensaje;
import cl.duoc.colegio.comunicaciones.model.EstadoMensaje;
import cl.duoc.colegio.comunicaciones.model.Mensaje;
import cl.duoc.colegio.comunicaciones.repository.MensajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
})
@ActiveProfiles("test")
class MensajeE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MensajeRepository mensajeRepository;

    @BeforeEach
    void limpiarBD() {
        mensajeRepository.deleteAll();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private Mensaje crearMensajeEnBD(Long estudianteId) {
        Mensaje m = new Mensaje();
        m.setEstudianteId(estudianteId);
        m.setApoderadoId(100L);
        m.setDescripcion("Conducta inapropiada");
        m.setTipo("GRAVE");
        m.setFechaAnotacion(LocalDate.now());
        m.setCanal(CanalMensaje.SISTEMA);
        m.setEstado(EstadoMensaje.ENVIADO);
        m.setFechaEnvio(LocalDateTime.now());
        return mensajeRepository.save(m);
    }

    @Test
    void debeRetornarListaVaciaSiNoHayMensajes() {
        ResponseEntity<Mensaje[]> response = restTemplate.getForEntity(
                url("/mensaje"), Mensaje[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }

    @Test
    void debeListarMensajesExistentes() {
        crearMensajeEnBD(1L);
        crearMensajeEnBD(2L);

        ResponseEntity<Mensaje[]> response = restTemplate.getForEntity(
                url("/mensaje"), Mensaje[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void debeFiltrarMensajesPorEstudiante() {
        crearMensajeEnBD(5L);
        crearMensajeEnBD(5L);
        crearMensajeEnBD(9L);

        ResponseEntity<Mensaje[]> response = restTemplate.getForEntity(
                url("/mensaje/estudiante/5"), Mensaje[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
        for (Mensaje m : response.getBody()) {
            assertEquals(5L, m.getEstudianteId());
        }
    }
}
