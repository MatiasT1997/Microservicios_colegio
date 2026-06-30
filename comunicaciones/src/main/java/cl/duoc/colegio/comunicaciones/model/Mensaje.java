package cl.duoc.colegio.comunicaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
@Data
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long estudianteId;

    private Long apoderadoId;

    private String descripcion;

    private String tipo;

    private LocalDate fechaAnotacion;

    @Enumerated(EnumType.STRING)
    private CanalMensaje canal;

    @Enumerated(EnumType.STRING)
    private EstadoMensaje estado;

    private LocalDateTime fechaEnvio;
}