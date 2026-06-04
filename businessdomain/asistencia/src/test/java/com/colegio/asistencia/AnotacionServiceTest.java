package com.colegio.asistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.colegio.asistencia.entities.Anotacion;
import com.colegio.asistencia.messaging.AnotacionEvent;
import com.colegio.asistencia.messaging.AnotacionEventPublisher;
import com.colegio.asistencia.repository.AnotacionRepository;
import com.colegio.asistencia.service.AnotacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Pruebas unitarias del servicio de anotaciones, verificando la publicacion de
 * eventos a RabbitMQ segun el tipo de anotacion.
 */
class AnotacionServiceTest {

    private AnotacionRepository repository;
    private AnotacionEventPublisher publisher;
    private AnotacionService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AnotacionRepository.class);
        publisher = Mockito.mock(AnotacionEventPublisher.class);
        service = new AnotacionService(repository, publisher);
        when(repository.save(any(Anotacion.class))).thenAnswer(inv -> {
            Anotacion a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });
    }

    @Test
    @DisplayName("Una anotacion GRAVE publica un evento a RabbitMQ")
    void anotacionGravePublicaEvento() {
        Anotacion a = new Anotacion();
        a.setTipo(Anotacion.Tipo.GRAVE);
        a.setEstudianteNombre("Juan Perez");
        a.setApoderadoId(50L);

        service.registrar(a);

        ArgumentCaptor<AnotacionEvent> captor = ArgumentCaptor.forClass(AnotacionEvent.class);
        verify(publisher).publicar(captor.capture());
        AnotacionEvent evento = captor.getValue();
        assertNotNull(evento);
        assertEquals("GRAVE", evento.getTipo());
        assertEquals("Juan Perez", evento.getEstudianteNombre());
    }

    @Test
    @DisplayName("Una anotacion POSITIVA NO publica evento")
    void anotacionPositivaNoPublica() {
        Anotacion a = new Anotacion();
        a.setTipo(Anotacion.Tipo.POSITIVA);

        service.registrar(a);

        verify(publisher, never()).publicar(any());
    }

    @Test
    @DisplayName("Si la anotacion no trae fecha, se asigna la fecha actual")
    void asignaFechaSiFalta() {
        Anotacion a = new Anotacion();
        a.setTipo(Anotacion.Tipo.POSITIVA);

        Anotacion guardada = service.registrar(a);

        assertNotNull(guardada.getFecha());
    }
}
