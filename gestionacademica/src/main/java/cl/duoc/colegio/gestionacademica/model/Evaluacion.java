package cl.duoc.colegio.gestionacademica.model;

import cl.duoc.colegio.gestionacademica.model.Asignatura;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "evaluacion")
@Data
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private TipoEvaluacion tipo;

    private Double ponderacion;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    @JsonIgnoreProperties("evaluaciones")
    private Asignatura asignatura;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("evaluacion")
    private List<Nota> notas;
}