package com.colegio.asistencia.messaging;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento publicado a RabbitMQ cuando se registra una anotacion que requiere
 * notificar al apoderado. Es el contrato compartido (logicamente) con el
 * consumidor en el microservicio Comunicaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnotacionEvent implements Serializable {
    private Long anotacionId;
    private Long estudianteId;
    private String estudianteNombre;
    private Long apoderadoId;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
}
