package cl.duoc.colegio.gestionacademica.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Entity
@Table(name = "asignatura")
@Data
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String docente;
    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnoreProperties("asignaturas")
    private Curso curso;
    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("asignatura")
    private List<Evaluacion> evaluaciones;
}