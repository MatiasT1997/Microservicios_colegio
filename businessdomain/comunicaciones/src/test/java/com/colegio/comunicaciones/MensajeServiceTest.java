package com.colegio.comunicaciones;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.colegio.comunicaciones.entities.Mensaje;
import com.colegio.comunicaciones.messaging.AnotacionEvent;
import com.colegio.comunicaciones.repository.MensajeRepository;
import com.colegio.comunicaciones.service.MensajeService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Pruebas unitarias de la generacion de notificaciones a partir de eventos.
 */
class MensajeServiceTest {

    private MensajeRepository repository;
    private MensajeService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(MensajeRepository.class);
        service = new MensajeService(repository);
        when(repository.save(any(Mensaje.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    @DisplayName("Genera una notificacion al apoderado a partir de un evento de anotacion")
    void creaNotificacionDesdeEvento() {
        AnotacionEvent evento = new AnotacionEvent();
        evento.setAnotacionId(1L);
        evento.setApoderadoId(50L);
        evento.setEstudianteNombre("Ana Soto");
        evento.setTipo("GRAVE");
        evento.setDescripcion("Inasistencia reiterada");
        evento.setFecha(LocalDateTime.now());

        Mensaje notificacion = service.crearDesdeAnotacion(evento);

        assertNotNull(notificacion);
        assertEquals(50L, notificacion.getDestinatarioId());
        assertEquals(Mensaje.Canal.NOTIFICACION_APP, notificacion.getCanal());
        assertEquals(Mensaje.Estado.PENDIENTE, notificacion.getEstado());
    }

    @Test
    @DisplayName("Al crear un mensaje sin estado/fecha, se asignan valores por defecto")
    void asignaDefaults() {
        Mensaje m = new Mensaje();
        m.setDestinatarioId(1L);

        Mensaje creado = service.crear(m);

        assertNotNull(creado.getFecha());
        assertEquals(Mensaje.Estado.PENDIENTE, creado.getEstado());
    }
}
