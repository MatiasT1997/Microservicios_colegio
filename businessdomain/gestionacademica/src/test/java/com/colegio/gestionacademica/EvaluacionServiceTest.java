package com.colegio.gestionacademica;
import com.colegio.gestionacademica.service.EvaluacionService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.colegio.gestionacademica.common.BusinessRuleException;
import com.colegio.gestionacademica.entities.Asignatura;
import com.colegio.gestionacademica.entities.Evaluacion;
import com.colegio.gestionacademica.entities.Evaluacion.TipoEvaluacion;
import com.colegio.gestionacademica.factory.EvaluacionFactoryProvider;
import com.colegio.gestionacademica.repository.AsignaturaRepository;
import com.colegio.gestionacademica.repository.EvaluacionRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Pruebas unitarias del servicio de evaluaciones usando mocks de los repositorios.
 */
class EvaluacionServiceTest {

    private EvaluacionRepository evaluacionRepository;
    private AsignaturaRepository asignaturaRepository;
    private EvaluacionService service;

    @BeforeEach
    void setUp() {
        evaluacionRepository = Mockito.mock(EvaluacionRepository.class);
        asignaturaRepository = Mockito.mock(AsignaturaRepository.class);
        service = new EvaluacionService(evaluacionRepository, asignaturaRepository, new EvaluacionFactoryProvider());
    }

    @Test
    @DisplayName("Crea evaluacion asociandola a la asignatura existente")
    void creaEvaluacionOk() {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Evaluacion resultado = service.crearEvaluacion(
                1L, TipoEvaluacion.PRUEBA, "Prueba 1", 30.0, LocalDate.of(2026, 5, 1));

        assertEquals(TipoEvaluacion.PRUEBA, resultado.getTipo());
        assertEquals(asignatura, resultado.getAsignatura());

        ArgumentCaptor<Evaluacion> captor = ArgumentCaptor.forClass(Evaluacion.class);
        verify(evaluacionRepository).save(captor.capture());
        assertEquals("Prueba 1", captor.getValue().getTitulo());
    }

    @Test
    @DisplayName("Si la asignatura no existe lanza BusinessRuleException y no persiste")
    void asignaturaInexistente() {
        when(asignaturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class, () ->
                service.crearEvaluacion(99L, TipoEvaluacion.TAREA, "X", 10.0, null));

        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Ponderacion fuera de rango (0-100) lanza BusinessRuleException")
    void ponderacionInvalida() {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(1L);
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));

        assertThrows(BusinessRuleException.class, () ->
                service.crearEvaluacion(1L, TipoEvaluacion.EXAMEN, "Examen", 150.0, null));

        verify(evaluacionRepository, never()).save(any());
    }
}
