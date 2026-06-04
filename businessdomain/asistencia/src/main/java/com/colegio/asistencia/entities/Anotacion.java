package com.colegio.asistencia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

/** Anotacion de conducta (positiva o negativa) sobre un estudiante. */
@Data
@Entity
public class Anotacion {

    public enum Tipo { POSITIVA, NEGATIVA, GRAVE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;
    private String estudianteNombre;
    private Long apoderadoId;
    private String descripcion;
    private String docente;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private LocalDateTime fecha;
}
