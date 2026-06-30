package cl.duoc.colegio.gestionacademica.model;

import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nota")
@Data
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id")
    @JsonIgnoreProperties("notas")
    private Evaluacion evaluacion;
}