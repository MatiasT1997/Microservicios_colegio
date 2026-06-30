package cl.duoc.colegio.asistencia.e2e;

import cl.duoc.colegio.asistencia.model.EstadoAsistencia;
import cl.duoc.colegio.asistencia.model.RegistroAsistencia;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas End-to-End - Asistencia: Flujo Registro de Asistencia
 * Levantan el contexto completo con servidor real y H2 en memoria.
 * RabbitTemplate se reemplaza con un mock para no necesitar RabbitMQ real.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RegistroAsistenciaE2ETest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void debeRegistrarAsistenciaYRecibirRespuesta200() {
        RegistroAsistencia registro = new RegistroAsistencia();
        registro.setEstudianteId(1L);
        registro.setCursoId(1L);
        registro.setFecha(LocalDate.now());
        registro.setEstado(EstadoAsistencia.PRESENTE);

        ResponseEntity<RegistroAsistencia> response = restTemplate.postForEntity(
                url("/asistencia"), registro, RegistroAsistencia.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EstadoAsistencia.PRESENTE, response.getBody().getEstado());
    }

    @Test
    void debeListarRegistrosDeAsistencia() {
        RegistroAsistencia registro = new RegistroAsistencia();
        registro.setEstudianteId(2L);
        registro.setCursoId(1L);
        registro.setFecha(LocalDate.now());
        registro.setEstado(EstadoAsistencia.AUSENTE);
        restTemplate.postForEntity(url("/asistencia"), registro, RegistroAsistencia.class);

        ResponseEntity<RegistroAsistencia[]> response = restTemplate.getForEntity(
                url("/asistencia"), RegistroAsistencia[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void debeFiltrarRegistrosPorEstudiante() {
        RegistroAsistencia r1 = new RegistroAsistencia();
        r1.setEstudianteId(10L);
        r1.setCursoId(1L);
        r1.setFecha(LocalDate.now());
        r1.setEstado(EstadoAsistencia.PRESENTE);
        restTemplate.postForEntity(url("/asistencia"), r1, RegistroAsistencia.class);

        ResponseEntity<RegistroAsistencia[]> response = restTemplate.getForEntity(
                url("/asistencia/estudiante/10"), RegistroAsistencia[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (RegistroAsistencia r : response.getBody()) {
            assertEquals(10L, r.getEstudianteId());
        }
    }
}
