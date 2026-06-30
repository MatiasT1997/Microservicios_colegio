package cl.duoc.colegio.comunicaciones.unitarias;

import cl.duoc.colegio.comunicaciones.messaging.AnotacionEvent;
import cl.duoc.colegio.comunicaciones.model.CanalMensaje;
import cl.duoc.colegio.comunicaciones.model.EstadoMensaje;
import cl.duoc.colegio.comunicaciones.model.Mensaje;
import cl.duoc.colegio.comunicaciones.repository.MensajeRepository;
import cl.duoc.colegio.comunicaciones.service.MensajeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias - MensajeService
 * Verifican que el servicio construya correctamente
 * un Mensaje a partir de un AnotacionEvent recibido.
 */
@ExtendWith(MockitoExtension.class)
class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @InjectMocks
    private MensajeService mensajeService;

    @Test
    void debeConstruirMensajeCorrectoDesdeEvento() {
        AnotacionEvent evento = new AnotacionEvent(
                1L, 100L, "Conducta inapropiada", "GRAVE",
                LocalDate.of(2026, 6, 28)
        );
        ArgumentCaptor<Mensaje> captor = ArgumentCaptor.forClass(Mensaje.class);
        when(mensajeRepository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        mensajeService.crearDesdeEvento(evento);

        verify(mensajeRepository).save(captor.capture());
        Mensaje guardado = captor.getValue();

        assertEquals(1L, guardado.getEstudianteId());
        assertEquals(100L, guardado.getApoderadoId());
        assertEquals("Conducta inapropiada", guardado.getDescripcion());
        assertEquals("GRAVE", guardado.getTipo());
        assertEquals(CanalMensaje.SISTEMA, guardado.getCanal());
        assertEquals(EstadoMensaje.ENVIADO, guardado.getEstado());
        assertNotNull(guardado.getFechaEnvio());
    }

    @Test
    void debePersistirElMensajeEnElRepositorio() {
        AnotacionEvent evento = new AnotacionEvent(
                2L, 200L, "Descripcion test", "NEGATIVA",
                LocalDate.now()
        );
        when(mensajeRepository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        mensajeService.crearDesdeEvento(evento);

        verify(mensajeRepository, times(1)).save(any(Mensaje.class));
    }

    @Test
    void debeRetornarListaVaciaSiNoHayMensajes() {
        when(mensajeRepository.findAll()).thenReturn(java.util.List.of());

        var resultado = mensajeService.listarTodos();

        assertTrue(resultado.isEmpty());
    }
}
