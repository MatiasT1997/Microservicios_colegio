package cl.duoc.colegio.gestionacademica.unitarias;

import cl.duoc.colegio.gestionacademica.factory.PruebaFactory;
import cl.duoc.colegio.gestionacademica.factory.ExamenFactory;
import cl.duoc.colegio.gestionacademica.factory.TareaFactory;
import cl.duoc.colegio.gestionacademica.factory.ProyectoFactory;
import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import cl.duoc.colegio.gestionacademica.model.TipoEvaluacion;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias - Patron Factory Method
 * Verifican que cada fabrica de evaluacion construya
 * correctamente los objetos segun su tipo.
 */
class PruebaFactoryTest {

    @Test
    void debeConstruirEvaluacionConTipoPrueba() {
        PruebaFactory factory = new PruebaFactory();
        LocalDate fecha = LocalDate.of(2026, 7, 15);

        Evaluacion resultado = factory.construir("Prueba unidad 1", 30.0, fecha);

        assertEquals("Prueba unidad 1", resultado.getTitulo());
        assertEquals(TipoEvaluacion.PRUEBA, resultado.getTipo());
        assertEquals(30.0, resultado.getPonderacion());
        assertEquals(fecha, resultado.getFecha());
    }

    @Test
    void debeUsarFechaActualSiFechaEsNull() {
        PruebaFactory factory = new PruebaFactory();

        Evaluacion resultado = factory.construir("Sin fecha", 20.0, null);

        assertEquals(LocalDate.now(), resultado.getFecha());
    }

    @Test
    void debeConstruirEvaluacionConTipoExamen() {
        ExamenFactory factory = new ExamenFactory();
        LocalDate fecha = LocalDate.of(2026, 12, 1);

        Evaluacion resultado = factory.construir("Examen final", 40.0, fecha);

        assertEquals(TipoEvaluacion.EXAMEN, resultado.getTipo());
        assertEquals("Examen final", resultado.getTitulo());
        assertEquals(40.0, resultado.getPonderacion());
    }

    @Test
    void debeConstruirEvaluacionConTipoTarea() {
        TareaFactory factory = new TareaFactory();

        Evaluacion resultado = factory.construir("Tarea 1", 10.0, LocalDate.now());

        assertEquals(TipoEvaluacion.TAREA, resultado.getTipo());
        assertNotNull(resultado.getFecha());
    }

    @Test
    void debeConstruirEvaluacionConTipoProyecto() {
        ProyectoFactory factory = new ProyectoFactory();

        Evaluacion resultado = factory.construir("Proyecto semestral", 50.0, LocalDate.now());

        assertEquals(TipoEvaluacion.PROYECTO, resultado.getTipo());
        assertEquals("Proyecto semestral", resultado.getTitulo());
    }
}
