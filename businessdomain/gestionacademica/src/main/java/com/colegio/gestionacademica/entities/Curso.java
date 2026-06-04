package com.colegio.gestionacademica.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

/**
 * Representa un curso del colegio (ej: "1ro Medio A").
 * Un curso agrupa varias asignaturas.
 */
@Data
@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;      // ej: "1ro Medio A"
    private String nivel;       // ej: "Enseñanza Media"
    private String anioLectivo; // ej: "2026"

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Asignatura> asignaturas = new ArrayList<>();
}
