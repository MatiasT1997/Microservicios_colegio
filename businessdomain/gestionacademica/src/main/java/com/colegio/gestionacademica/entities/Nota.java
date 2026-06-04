package com.colegio.gestionacademica.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Nota obtenida por un estudiante en una evaluacion.
 * El estudiante se referencia por su id (rut interno); el detalle del estudiante
 * vive en otro contexto, manteniendo el principio de BD por servicio.
 */
@Data
@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;   // referencia logica al estudiante
    private String estudianteNombre;
    private Double valor;        // escala 1.0 - 7.0 (sistema chileno)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id")
    @JsonBackReference
    private Evaluacion evaluacion;
}
