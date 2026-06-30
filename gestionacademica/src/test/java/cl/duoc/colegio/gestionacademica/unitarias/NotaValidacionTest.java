package cl.duoc.colegio.gestionacademica.unitarias;

import cl.duoc.colegio.gestionacademica.controller.NotaController;
import cl.duoc.colegio.gestionacademica.exception.BusinessRuleException;
import cl.duoc.colegio.gestionacademica.model.Nota;
import cl.duoc.colegio.gestionacademica.repository.NotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias - Validacion de negocio en NotaController
 * Verifican que la logica de validacion de rango (1.0 a 7.0)
 * funcione correctamente sin necesitar base de datos real.
 */
@ExtendWith(MockitoExtension.class)
class NotaValidacionTest {

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private NotaController notaController;

    private Nota notaValida;

    @BeforeEach
    void setUp() {
        notaValida = new Nota();
        notaValida.setEstudianteId(1L);
        notaValida.setValor(5.5);
    }

    @Test
    void debeRegistrarNotaConValorValido() {
        when(notaRepository.save(notaValida)).thenReturn(notaValida);

        Nota resultado = notaController.registrar(1L, notaValida);

        assertNotNull(resultado);
        assertEquals(5.5, resultado.getValor());
        verify(notaRepository, times(1)).save(notaValida);
    }

    @Test
    void debeLanzarExcepcionSiNotaMenorAlMinimo() {
        Nota notaInvalida = new Nota();
        notaInvalida.setValor(0.9);

        assertThrows(BusinessRuleException.class,
                () -> notaController.registrar(1L, notaInvalida));

        verify(notaRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNotaMayorAlMaximo() {
        Nota notaInvalida = new Nota();
        notaInvalida.setValor(7.1);

        assertThrows(BusinessRuleException.class,
                () -> notaController.registrar(1L, notaInvalida));

        verify(notaRepository, never()).save(any());
    }

    @Test
    void debeAceptarNotaEnElLimiteMinimo() {
        Nota notaMinima = new Nota();
        notaMinima.setValor(1.0);
        when(notaRepository.save(notaMinima)).thenReturn(notaMinima);

        Nota resultado = notaController.registrar(1L, notaMinima);

        assertEquals(1.0, resultado.getValor());
    }

    @Test
    void debeAceptarNotaEnElLimiteMaximo() {
        Nota notaMaxima = new Nota();
        notaMaxima.setValor(7.0);
        when(notaRepository.save(notaMaxima)).thenReturn(notaMaxima);

        Nota resultado = notaController.registrar(1L, notaMaxima);

        assertEquals(7.0, resultado.getValor());
    }
}
