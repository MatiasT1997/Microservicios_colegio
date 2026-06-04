package com.colegio.comunicaciones.messaging;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contrato del evento recibido desde Asistencia via RabbitMQ.
 * Debe ser compatible (mismos campos) con el publicado por el productor.
 */
@Data
@NoArgsConstructor
public class AnotacionEvent implements Serializable {
    private Long anotacionId;
    private Long estudianteId;
    private String estudianteNombre;
    private Long apoderadoId;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
}
