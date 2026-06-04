package com.colegio.asistencia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Data;

/** Registro diario de asistencia de un estudiante a una asignatura/curso. */
@Data
@Entity
public class RegistroAsistencia {

    public enum Estado { PRESENTE, AUSENTE, ATRASADO, JUSTIFICADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;
    private String estudianteNombre;
    private Long cursoId;
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
