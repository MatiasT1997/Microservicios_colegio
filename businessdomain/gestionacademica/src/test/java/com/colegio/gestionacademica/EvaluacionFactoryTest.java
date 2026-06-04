package com.colegio.gestionacademica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.colegio.gestionacademica.entities.Evaluacion;
import com.colegio.gestionacademica.entities.Evaluacion.TipoEvaluacion;
import com.colegio.gestionacademica.factory.EvaluacionFactory;
import com.colegio.gestionacademica.factory.EvaluacionFactoryProvider;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patron Factory Method para evaluaciones.
 */
class EvaluacionFactoryTest {

    private final EvaluacionFactoryProvider provider = new EvaluacionFactoryProvider();

    @Test
    @DisplayName("La fabrica de PRUEBA crea una evaluacion tipo PRUEBA con datos comunes")
    void creaPrueba() {
        EvaluacionFactory factory = provider.getFactory(TipoEvaluacion.PRUEBA);
        Evaluacion e = factory.construir("Prueba Unidad 1", 30.0, LocalDate.of(2026, 4, 10));

        assertNotNull(e);
        assertEquals(TipoEvaluacion.PRUEBA, e.getTipo());
        assertEquals("Prueba Unidad 1", e.getTitulo());
        assertEquals(30.0, e.getPonderacion());
        assertEquals(LocalDate.of(2026, 4, 10), e.getFecha());
    }

    @Test
    @DisplayName("Cada tipo produce el TipoEvaluacion correcto")
    void cadaTipoProduceSuTipo() {
        assertEquals(TipoEvaluacion.TAREA,
                provider.getFactory(TipoEvaluacion.TAREA).construir("T", 10.0, null).getTipo());
        assertEquals(TipoEvaluacion.EXAMEN,
                provider.getFactory(TipoEvaluacion.EXAMEN).construir("E", 40.0, null).getTipo());
        assertEquals(TipoEvaluacion.CONTROL,
                provider.getFactory(TipoEvaluacion.CONTROL).construir("C", 5.0, null).getTipo());
    }

    @Test
    @DisplayName("Si no se entrega fecha, la fabrica asigna la fecha actual")
    void asignaFechaPorDefecto() {
        Evaluacion e = provider.getFactory(TipoEvaluacion.TAREA).construir("Tarea", 15.0, null);
        assertEquals(LocalDate.now(), e.getFecha());
    }

    @Test
    @DisplayName("Solicitar un tipo nulo o no registrado lanza excepcion")
    void tipoInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> provider.getFactory(null));
    }
}
