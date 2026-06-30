package cl.duoc.colegio.gestionacademica.factory;

import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import cl.duoc.colegio.gestionacademica.model.TipoEvaluacion;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class TareaFactory implements EvaluacionFactory {

    @Override
    public Evaluacion construir(String titulo, Double ponderacion, LocalDate fecha) {
        Evaluacion e = new Evaluacion();
        e.setTitulo(titulo);
        e.setTipo(TipoEvaluacion.TAREA);
        e.setPonderacion(ponderacion);
        e.setFecha(fecha != null ? fecha : LocalDate.now());
        return e;
    }
}