package cl.duoc.colegio.comunicaciones.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnotacionEvent implements Serializable {
    private Long estudianteId;
    private Long apoderadoId;
    private String descripcion;
    private String tipo;
    private LocalDate fecha;
}