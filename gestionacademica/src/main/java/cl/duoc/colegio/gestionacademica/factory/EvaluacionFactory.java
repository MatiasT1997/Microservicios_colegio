package cl.duoc.colegio.gestionacademica.factory;

import cl.duoc.colegio.gestionacademica.model.Evaluacion;
import java.time.LocalDate;

public interface EvaluacionFactory {
    Evaluacion construir(String titulo, Double ponderacion, LocalDate fecha);
}