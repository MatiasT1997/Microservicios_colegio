package cl.duoc.colegio.asistencia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "anotacion")
@Data
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;
    private Long apoderadoId;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoAnotacion tipo;

    private LocalDate fecha;
}