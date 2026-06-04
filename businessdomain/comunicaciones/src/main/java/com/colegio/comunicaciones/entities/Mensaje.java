package com.colegio.comunicaciones.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

/** Mensaje o notificacion dirigido a un destinatario (apoderado, docente, etc.). */
@Data
@Entity
public class Mensaje {

    public enum Canal { NOTIFICACION_APP, EMAIL, SMS }
    public enum Estado { PENDIENTE, ENVIADO, LEIDO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long destinatarioId;     // ej: apoderadoId
    private String destinatarioNombre;
    private String asunto;
    private String contenido;

    @Enumerated(EnumType.STRING)
    private Canal canal;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDateTime fecha;
}
