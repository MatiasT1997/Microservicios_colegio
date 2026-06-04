package com.colegio.gestionacademica.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Evaluacion asociada a una asignatura.
 * El tipo de evaluacion (PRUEBA, TAREA, EXAMEN) se modela con un enum y se
 * construye mediante el patron Factory Method (ver paquete factory).
 */
@Data
@Entity
public class Evaluacion {

    public enum TipoEvaluacion {
        PRUEBA, TAREA, EXAMEN, CONTROL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private TipoEvaluacion tipo;

    private Double ponderacion;   // peso porcentual de la evaluacion (0-100)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id")
    @JsonBackReference
    private Asignatura asignatura;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Nota> notas = new ArrayList<>();
}
