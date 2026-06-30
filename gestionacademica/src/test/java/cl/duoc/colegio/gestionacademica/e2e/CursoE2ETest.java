package cl.duoc.colegio.gestionacademica.e2e;

import cl.duoc.colegio.gestionacademica.model.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas End-to-End - Gestion Academica: Flujo Curso
 * Levantan el contexto completo de Spring Boot con servidor
 * HTTP real (puerto aleatorio) y base de datos H2 en memoria.
 * Simulan el comportamiento completo de la API REST.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CursoE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void debeCrearCursoYRecibirRespuesta200() {
        Curso curso = new Curso();
        curso.setNombre("1ro Medio A");
        curso.setNivel("Ensenanza Media");
        curso.setAnioLectivo(2026);

        ResponseEntity<Curso> response = restTemplate.postForEntity(
                url("/curso"), curso, Curso.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("1ro Medio A", response.getBody().getNombre());
    }

    @Test
    void debeListarCursosYRecibirArregloJSON() {
        Curso curso = new Curso();
        curso.setNombre("2do Medio B");
        curso.setNivel("Ensenanza Media");
        curso.setAnioLectivo(2026);
        restTemplate.postForEntity(url("/curso"), curso, Curso.class);

        ResponseEntity<Curso[]> response = restTemplate.getForEntity(
                url("/curso"), Curso[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void debeObtenerCursoPorIdExistente() {
        Curso curso = new Curso();
        curso.setNombre("3ro Medio C");
        curso.setNivel("Ensenanza Media");
        curso.setAnioLectivo(2026);
        Curso creado = restTemplate.postForEntity(
                url("/curso"), curso, Curso.class).getBody();

        ResponseEntity<Curso> response = restTemplate.getForEntity(
                url("/curso/" + creado.getId()), Curso.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("3ro Medio C", response.getBody().getNombre());
    }
}
