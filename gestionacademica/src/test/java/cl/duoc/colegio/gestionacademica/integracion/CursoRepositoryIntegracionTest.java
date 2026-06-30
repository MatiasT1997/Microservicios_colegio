package cl.duoc.colegio.gestionacademica.integracion;

import cl.duoc.colegio.gestionacademica.model.Curso;
import cl.duoc.colegio.gestionacademica.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integracion - CursoRepository
 * Verifican que las operaciones de persistencia sobre la
 * entidad Curso funcionen correctamente con H2 en memoria.
 */
@DataJpaTest
@ActiveProfiles("test")
class CursoRepositoryIntegracionTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void debeGuardarYRecuperarCurso() {
        Curso curso = new Curso();
        curso.setNombre("1ro Medio A");
        curso.setNivel("Ensenanza Media");
        curso.setAnioLectivo(2026);

        Curso guardado = cursoRepository.save(curso);
        Curso recuperado = cursoRepository.findById(guardado.getId()).orElse(null);

        assertNotNull(recuperado);
        assertEquals("1ro Medio A", recuperado.getNombre());
        assertEquals("Ensenanza Media", recuperado.getNivel());
        assertEquals(2026, recuperado.getAnioLectivo());
    }

    @Test
    void debeListarTodosLosCursos() {
        Curso c1 = new Curso();
        c1.setNombre("1ro Medio A");
        c1.setNivel("Ensenanza Media");
        c1.setAnioLectivo(2026);

        Curso c2 = new Curso();
        c2.setNombre("2do Medio B");
        c2.setNivel("Ensenanza Media");
        c2.setAnioLectivo(2026);

        cursoRepository.save(c1);
        cursoRepository.save(c2);

        List<Curso> cursos = cursoRepository.findAll();

        assertEquals(2, cursos.size());
    }

    @Test
    void debeEliminarCurso() {
        Curso curso = new Curso();
        curso.setNombre("3ro Medio A");
        curso.setNivel("Ensenanza Media");
        curso.setAnioLectivo(2026);

        Curso guardado = cursoRepository.save(curso);
        cursoRepository.deleteById(guardado.getId());

        assertFalse(cursoRepository.findById(guardado.getId()).isPresent());
    }
}
